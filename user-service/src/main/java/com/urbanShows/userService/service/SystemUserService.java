package com.urbanShows.userService.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

//import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.constants.TableConfig;
import com.urbanShows.userService.dto.ColumnConfigDto;
import com.urbanShows.userService.dto.SearchRequest;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.dto.SystemUserInfoRespone;
import com.urbanShows.userService.dto.SystemUserRegisterDto;
import com.urbanShows.userService.dto.UserPage;
import com.urbanShows.userService.dto.UserSecuredDetailsReq;
import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.SortOrder;
import com.urbanShows.userService.enums.Status;
import com.urbanShows.userService.exception.GenericException;
import com.urbanShows.userService.exception.IncorrectOtpException;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.exception.UserAlreadyExistsException;
import com.urbanShows.userService.exception.UserNotFoundException;
import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.kafka.OtpkafkaDto;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.repository.UserInfoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SystemUserService {

	private final UserInfoRepository userInfoRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final MessageProducer messageProducer;
	private final OtpService otpService;

	public List<String> getOrganizerList() {
		final List<SystemUser> systemUserList = userInfoRepository.findByRolesAndStatus(List.of(Role.ORGANIZER_USER),
				Status.ACTIVE);
		if (!systemUserList.isEmpty()) {
			log.info("Organizer list fetched successfully");
			return systemUserList.stream().map(SystemUser::getUserName).toList();
		}
		log.error("No organizer found");
		throw new UserNotFoundException("user not found");
	}

	public Boolean isValidOrganizer(String userName) {
		final SystemUser systemUserList = userInfoRepository.findByUserNameAndRolesAndStatus(userName,
				List.of(Role.ORGANIZER_USER), Status.ACTIVE);
		if (systemUserList == null) {
			log.error("No organizer found with userName: {}", userName);
			throw new UserNotFoundException("user not found");
		}
		log.info("Valid organizer found with userName: {}", userName);
		return systemUserList != null;
	}

	public void uploadProfilePicUrl(SystemUserInfoDto systemUserDto, String profilePicUrl) {
		final SystemUser appUser = new SystemUser();
		appUser.setUserName(systemUserDto.getUserName());
		appUser.setProfilePicUrl(profilePicUrl);
		userInfoRepository.save(appUser);
		log.info("Profile picture uploaded for user: {}", systemUserDto.getUserName());
	}

	public SystemUser validateActiveSystemUserByOtp(String userName, String otp) {
		final SystemUser systemUser = userInfoRepository.findByUserNameAndOtpAndStatus(userName, otp, Status.ACTIVE);
		if (systemUser == null) {
			log.error("Invalid OTP for user: {}", userName);
			throw new IncorrectOtpException("OTP is not correct or expired or user is inactive");
		}
		log.info("OTP validated successfully for user: {}", userName);
		return systemUser;
	}

	public SystemUser validateInactiveSystemUserByOtp(String userName, String otp) {
		final SystemUser systemUser = userInfoRepository.findByUserNameAndOtpAndStatus(userName, otp, Status.INACTIVE);
		if (systemUser == null) {
			log.error("Invalid OTP for user: {}", userName);
			throw new IncorrectOtpException("OTP is not correct or expired or user is inactive");
		}
		log.info("OTP validated successfully for user: {}", userName);
		return systemUser;
	}

	public void uploadSystemUserProfilePicUrl(SystemUser systemUser, String profilePicUrl) {
		systemUser.setProfilePicUrl(profilePicUrl);
		userInfoRepository.save(systemUser);
	}

	public Boolean registerSystemUser(SystemUserRegisterDto systemUserSigninDto) {
		if (systemUserSigninDto.getRoles().contains(Role.SUPER_ADMIN_USER)) {
			final List<SystemUser> usersByRoles = userInfoRepository.findByRoles(systemUserSigninDto.getRoles());
			if (!usersByRoles.isEmpty()) {
				log.error("Super admin user registration attempt blocked");
				throw new UnauthorizedException("You are not authorized to perform this operation");
			}
		}
		if (userInfoRepository.existsById(systemUserSigninDto.getUserName())) {
			log.error("User registration failed. User already exists: {}", systemUserSigninDto.getUserName());
			throw new UserAlreadyExistsException("User already exists in the system");
		}
		final SystemUser systemUser = new SystemUser();
		final List<Role> roleList = new ArrayList<>();
		roleList.addAll(systemUserSigninDto.getRoles());
		systemUser.setRoles(roleList);
		systemUser.setPassword(passwordEncoder.encode(systemUserSigninDto.getPassword().trim()));
		systemUser.setDisplayName(systemUserSigninDto.getDisplayName().trim());
		systemUser.setUserName(systemUserSigninDto.getUserName().trim());
		systemUser.setEmail(systemUserSigninDto.getEmail().trim());
		systemUser.setPhone(systemUserSigninDto.getPhone().trim());
		systemUser.setStatus(Status.INACTIVE);
		roleList.stream().filter(i -> i.equals(Role.SUPER_ADMIN_USER)).forEach(i -> {
			systemUser.setStatus(Status.ACTIVE);
			systemUser.setPhoneValidated(true);
			systemUser.setEmailValidated(true);
		});
		systemUser.setCreatedAt(LocalDateTime.now());
		userInfoRepository.save(systemUser);
		log.info("System User {} is register in the system ", systemUserSigninDto.getUserName());
		return true;
	}

	public SystemUserInfoRespone getSystemUsersList(SearchRequest searchRequest) {
		final Pageable pageable = buildPage(searchRequest);
		final Specification<SystemUser> spec = UserSpecification.buildSpecification(searchRequest.getSearchFilters());
		Page<SystemUser> list = new PageImpl<>(new ArrayList<>());
		try {
			list = userInfoRepository.findAll(spec, pageable);
			log.info("User list fetched successfully");
		} catch (Exception e) {
			log.error("Error in fetching user list: {}", e.getMessage());
			throw new GenericException("Error in fetching user list");
		}
		return pageableEventResponse(list, spec, searchRequest.getCurrentPage());
	}

	@Transactional
	public void deleteSystemUserByUserName(SystemUserInfoDto systemUserDto) {
		final GenericMapper<SystemUserInfoDto, SystemUser> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUser.class);
		userInfoRepository.delete(mapper.dtoToEntity(systemUserDto));
		log.info("System user {} deleted successfully", systemUserDto.getUserName());
	}

	public boolean udpateSecuredUserDetails(UserSecuredDetailsReq securedDetailsReq, SystemUser targetUser,
			SystemUser currentUser) {
		final GenericMapper<UserSecuredDetailsReq, SystemUserInfoDto> mapper = new GenericMapper<>(modelMapper,
				UserSecuredDetailsReq.class, SystemUserInfoDto.class);
		final SystemUserInfoDto dtoToEntity = mapper.dtoToEntity(securedDetailsReq);
		log.info("Updating secured details for user: {}", targetUser.getUserName());
		return updateSecuredDetails(dtoToEntity, targetUser, currentUser);
	}

//	public boolean udpateBasicUserDetails(UserBasicDetails basicDetails, UserInfo targetUser) {
//		targetUser.setDisplayName(StringUtils.hasText(basicDetails.getDisplayName())
//				&& !targetUser.getDisplayName().equals(basicDetails.getDisplayName())
//						? basicDetails.getDisplayName().trim()
//						: targetUser.getDisplayName());
//
//		if (basicDetails.getProfilePicFile() != null) {
//			final String fileUrl = azureBlobStorageService.uploadFile(basicDetails.getProfilePicFile());
//			uploadSystemUserProfilePicUrl(targetUser, fileUrl);
//		}
//
//		userInfoRepository.save(targetUser);
//		return true;
//	}

	public void generateOtpForSystemUser(String userName, String device) {
		final SystemUser systemUser = getActiveExistingSystemUser(userName);
		otpService.createAndSendOtp(systemUser, device);
		log.info("OTP generated and sent for user: {}", userName);
	}

	public void sendOtpToPhone(String phone, String otp) {
		final OtpkafkaDto otpkafkaDto = new OtpkafkaDto(phone, otp);
		messageProducer.sendOtpMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), otpkafkaDto);
		log.info("OTP message sent to Kafka for phone: {}", phone);
	}

	private boolean updateSecuredDetails(SystemUserInfoDto newUser, SystemUser existingUser, SystemUser loggedInUser) {
		boolean shouldLoggedOut = false;
		if (!isSelfChange(existingUser.getUserName(), loggedInUser.getUserName())) {
			Status newStatus = !newUser.getStatus().equals(existingUser.getStatus()) ? newUser.getStatus()
					: existingUser.getStatus();
			existingUser.setStatus(newStatus);
			log.info("User {} status changed to {}", existingUser.getUserName(), newStatus);
		}
		if (StringUtils.hasText(newUser.getPassword())) {
			existingUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
			shouldLoggedOut = isSelfChange(existingUser.getUserName(), loggedInUser.getUserName());
			log.info("User {} password updated", existingUser.getUserName());
		}
		if (newUser.isPhoneValidated()) {
			existingUser.setPhoneValidated(true);
			log.info("User {} phone validated", existingUser.getUserName());
		}
		if (newUser.isEmailValidated()) {
			existingUser.setEmailValidated(true);
			log.info("User {} email validated", existingUser.getUserName());
		}
		if (StringUtils.hasText(newUser.getPhone()) && !existingUser.getPhone().equals(newUser.getPhone())) {
			existingUser.setPhone(newUser.getPhone());
			existingUser.setPhoneValidated(false);
			existingUser.setStatus(Status.INACTIVE);
			log.info("User {} phone number changed, validation reset", existingUser.getUserName());
		}
		if (StringUtils.hasText(newUser.getEmail()) && !existingUser.getEmail().equals(newUser.getEmail())) {
			existingUser.setEmail(newUser.getEmail());
			existingUser.setEmailValidated(false);
			existingUser.setStatus(Status.INACTIVE);
			log.info("User {} email changed, validation reset", existingUser.getUserName());
		}

		// for super admin all validations are true
		if (existingUser.getRoles().contains(Role.SUPER_ADMIN_USER)
				&& loggedInUser.getRoles().contains(Role.SUPER_ADMIN_USER)) {
			existingUser.setStatus(Status.ACTIVE);
			existingUser.setPhoneValidated(true);
			existingUser.setEmailValidated(true);
			log.info("Super admin user {} validations set to true", existingUser.getUserName());
		}
		userInfoRepository.save(existingUser);
		log.info("Secured details updated for user: {}", existingUser.getUserName());
		return shouldLoggedOut;
	}

	private boolean isSelfChange(String targetUserName, String loggedInUserName) {
		return targetUserName.equals(loggedInUserName);
	}

	public SystemUser getExistingSystemUser(String userName) {
		final SystemUser existingUser = userInfoRepository.findByUserName(userName);
		if (existingUser == null) {
			log.error("User not found: {}", userName);
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingUser;
	}

	public SystemUser getActiveExistingSystemUser(String userName) {
		final SystemUser existingUser = userInfoRepository.findByUserNameAndStatus(userName, Status.ACTIVE);
		if (existingUser == null) {
			log.error("Active user not found: {}", userName);
			throw new UserNotFoundException("User inctive/doesnot exists in the system");
		}
		return existingUser;
	}

	public boolean activateUser(String userName, String otp) {
		final SystemUser userInfo = validateInactiveSystemUserByOtp(userName, otp);
		return userInfo != null;
	}

	private Pageable buildPage(SearchRequest searchDto) {
		Pageable pageable = PageRequest.of(0, TableConfig.PAGE_SIZE);
		if (StringUtils.hasText(searchDto.getSortColumn())) {
			final Sort sort = searchDto.getSortOrder().equals(SortOrder.DESC)
					? Sort.by(searchDto.getSortColumn()).descending()
					: Sort.by(searchDto.getSortColumn()).ascending();
			pageable = PageRequest.of(searchDto.getCurrentPage(), TableConfig.PAGE_SIZE, sort);
		}
		return pageable;
	}

	private SystemUserInfoRespone pageableEventResponse(Page<SystemUser> pagedEventList, Specification<SystemUser> spec,
			int currentPage) {
		final GenericMapper<SystemUserInfoDto, SystemUser> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUser.class);
		final SystemUserInfoRespone eventDtoList = new SystemUserInfoRespone();
		final ColumnConfigDto columnConfig = new ColumnConfigDto(TableConfig.USER_COlUMNS);
		eventDtoList.setColumnConfig(columnConfig);
		eventDtoList.setUserInfoList(mapper.entityToDto(pagedEventList.getContent()));
		eventDtoList.setUserPage(buildEventPage(spec, currentPage, pagedEventList.getContent().size()));
		return eventDtoList;
	}

	private UserPage buildEventPage(Specification<SystemUser> spec, int currentPage, int currentRecordSize) {
		final UserPage eventPage = new UserPage();
		final int filteredCount = getEventCount(spec);
		eventPage.setTotalPages((int) Math.ceil((double) filteredCount / TableConfig.PAGE_SIZE));
		eventPage.setFilteredRecords(filteredCount);
		eventPage.setTotalRecords(getEventCount(null));
		eventPage.setRecordsPerPage(TableConfig.PAGE_SIZE);
		eventPage.setCurrentPage(currentPage);
		final int rowStartIndex = currentPage * TableConfig.PAGE_SIZE;
		eventPage.setRowStartIndex(rowStartIndex + 1);
		eventPage.setRowEndIndex(rowStartIndex + currentRecordSize);
		eventPage.setDisplayPagesIndex(getDisplayPagesIndex(eventPage.getTotalPages(), eventPage.getCurrentPage()));
		return eventPage;
	}

	private int getEventCount(Specification<SystemUser> spec) {
		return spec != null ? (int) userInfoRepository.count(spec) : (int) userInfoRepository.count();
	}

	private List<Integer> getDisplayPagesIndex(int totalPages, int currentPage) {
		List<Integer> displayPagesIndex = new ArrayList<>();

		if (totalPages <= 0 || currentPage < 0 || currentPage >= totalPages) {
			final Integer[] array = { 1, 2, 3, 4, 5 };
			displayPagesIndex = new ArrayList<>(Arrays.asList(array));
		} else {
			int start = Math.max(0, Math.min(currentPage - 2, totalPages - 5));
			int end = Math.min(totalPages, start + 5);

			for (int i = start; i < end; i++) {
				displayPagesIndex.add(i);
			}
		}
		return displayPagesIndex;
	}

}
