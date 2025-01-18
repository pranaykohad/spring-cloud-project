package com.urbanShows.userService.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.constants.TableConfig;
import com.urbanShows.userService.dto.ColumnConfigDto;
import com.urbanShows.userService.dto.SearchRequest;
import com.urbanShows.userService.dto.UserBasicDetails;
import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.dto.UserInfoRespone;
import com.urbanShows.userService.dto.UserPage;
import com.urbanShows.userService.dto.UserSecuredDetailsReq;
import com.urbanShows.userService.dto.UserSigninDto;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.SortOrder;
import com.urbanShows.userService.enums.Status;
import com.urbanShows.userService.exception.AccessDeniedException;
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
public class UserService {

	private final UserInfoRepository userInfoRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final MessageProducer messageProducer;
	private final OtpService otpService;
	private final AzureBlobStorageService azureBlobStorageService;

	public List<String> getOrganizerList() {
		final List<UserInfo> systemUserList = userInfoRepository.findByRolesAndStatus(List.of(Role.ORGANIZER_USER),
				Status.ACTIVE);
		if (!systemUserList.isEmpty()) {
			return systemUserList.stream().map(UserInfo::getUserName).toList();
		}
		throw new UserNotFoundException("user not found");
	}

	public Boolean isValidOrganizer(String userName) {
		final UserInfo systemUserList = userInfoRepository.findByUserNameAndRolesAndStatus(userName,
				List.of(Role.ORGANIZER_USER), Status.ACTIVE);
		if (systemUserList == null) {
			throw new UserNotFoundException("user not found");
		}
		return systemUserList != null;
	}

	public void uploadProfilePicUrl(UserInfoDto systemUserDto, String profilePicUrl) {
		final UserInfo appUser = new UserInfo();
		appUser.setUserName(systemUserDto.getUserName());
		appUser.setProfilePicUrl(profilePicUrl);
		userInfoRepository.save(appUser);
	}

	public UserInfo validateActiveSystemUserByOtp(String userName, String otp) {
		final UserInfo systemUser = userInfoRepository.findByUserNameAndOtpAndStatus(userName, otp, Status.ACTIVE);
		if (systemUser == null) {
			throw new IncorrectOtpException("OTP is not correct or expired or user is inactive");
		}
		return systemUser;
	}

	public UserInfo validateInactiveSystemUserByOtp(String userName, String otp) {
		final UserInfo systemUser = userInfoRepository.findByUserNameAndOtpAndStatus(userName, otp, Status.INACTIVE);
		if (systemUser == null) {
			throw new IncorrectOtpException("OTP is not correct or expired or user is inactive");
		}
		return systemUser;
	}

	public void uploadSystemUserProfilePicUrl(UserInfo systemUser, String profilePicUrl) {
		systemUser.setProfilePicUrl(profilePicUrl);
		userInfoRepository.save(systemUser);
	}

	public Boolean addSystemUser(UserSigninDto systemUserSigninDto) {
		if (systemUserSigninDto.getRoles().contains(Role.SUPER_ADMIN_USER)) {
			final List<UserInfo> usersByRoles = userInfoRepository.findByRoles(systemUserSigninDto.getRoles());
			if (!usersByRoles.isEmpty()) {
				throw new UnauthorizedException("You are not authorized to perform this operation");
			}
		}
		if (userInfoRepository.existsById(systemUserSigninDto.getUserName())) {
			throw new UserAlreadyExistsException("User already exists in the system");
		}
		final UserInfo systemUser = new UserInfo();
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

	public UserInfoRespone getSystemUsersList(SearchRequest searchRequest) {
		final Pageable pageable = buildPage(searchRequest);
		final Specification<UserInfo> spec = UserSpecification.buildSpecification(searchRequest.getSearchFilters());
		final Page<UserInfo> list = userInfoRepository.findAll(spec, pageable);
		return pageableEventResponse(list, spec, searchRequest.getCurrentPage());
	}

	@Transactional
	public void deleteSystemUserByUserName(UserInfoDto systemUserDto) {
		final GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		userInfoRepository.delete(mapper.dtoToEntity(systemUserDto));
	}

	public boolean udpateSecuredUserDetails(UserSecuredDetailsReq securedDetailsReq, UserInfo targetUser,
			UserInfo currentUser) {
		final GenericMapper<UserSecuredDetailsReq, UserInfoDto> mapper = new GenericMapper<>(modelMapper,
				UserSecuredDetailsReq.class, UserInfoDto.class);
		final UserInfoDto dtoToEntity = mapper.dtoToEntity(securedDetailsReq);
		updateSecuredUserDetails(dtoToEntity, targetUser, currentUser);
		return true;
	}

	public void generateOtpForSystemUser(String userName, String device) {
		final UserInfo systemUser = getActiveExistingSystemUser(userName);
		otpService.createAndSendOtp(systemUser, device);
	}

	public void sendOtpToPhone(String phone, String otp) {
		final OtpkafkaDto otpkafkaDto = new OtpkafkaDto(phone, otp);
		messageProducer.sendOtpMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), otpkafkaDto);
	}

	public boolean udpateBasicUserDetails(UserBasicDetails basicDetails, UserInfo targetUser) {
		targetUser.setDisplayName(StringUtils.hasText(basicDetails.getDisplayName())
				&& !targetUser.getDisplayName().equals(basicDetails.getDisplayName())
						? basicDetails.getDisplayName().trim()
						: targetUser.getDisplayName());

		if (basicDetails.getProfilePicFile() != null) {
			final String fileUrl = azureBlobStorageService.uploadFile(basicDetails.getProfilePicFile());
			uploadSystemUserProfilePicUrl(targetUser, fileUrl);
		}

		userInfoRepository.save(targetUser);
		return true;
	}

	private void updateSecuredUserDetails(UserInfoDto newUserInfo, UserInfo targetUserInfo, UserInfo currentUserInfo) {
		if (!currentUserInfo.getUserName().equals(targetUserInfo.getUserName())) {
			targetUserInfo
					.setStatus(!newUserInfo.getStatus().equals(targetUserInfo.getStatus()) ? newUserInfo.getStatus()
							: targetUserInfo.getStatus());
		}
		if (StringUtils.hasText(newUserInfo.getPassword())) {
			targetUserInfo.setPassword(passwordEncoder.encode(newUserInfo.getPassword()));
		}
		if (newUserInfo.isPhoneValidated()) {
			targetUserInfo.setPhoneValidated(true);
		}
		if (newUserInfo.isEmailValidated()) {
			targetUserInfo.setEmailValidated(true);
		}
		if (StringUtils.hasText(newUserInfo.getPhone()) && !targetUserInfo.getPhone().equals(newUserInfo.getPhone())) {
			targetUserInfo.setPhone(newUserInfo.getPhone());
			targetUserInfo.setPhoneValidated(false);
			targetUserInfo.setStatus(Status.INACTIVE);
		}
		if (StringUtils.hasText(newUserInfo.getEmail()) && !targetUserInfo.getEmail().equals(newUserInfo.getEmail())) {
			targetUserInfo.setEmail(newUserInfo.getEmail());
			targetUserInfo.setEmailValidated(false);
			targetUserInfo.setStatus(Status.INACTIVE);
		}
		
		// for super admin all validations are true
		if (targetUserInfo.getRoles().contains(Role.SUPER_ADMIN_USER)
				&& currentUserInfo.getRoles().contains(Role.SUPER_ADMIN_USER)) {
			targetUserInfo.setStatus(Status.ACTIVE);
			targetUserInfo.setPhoneValidated(true);
			targetUserInfo.setEmailValidated(true); 
		}
		userInfoRepository.save(targetUserInfo);
	}

	public UserInfo getExistingSystemUser(String userName) {
		final UserInfo existingUser = userInfoRepository.findByUserName(userName);
		if (existingUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingUser;
	}

	public UserInfo getActiveExistingSystemUser(String userName) {
		final UserInfo existingUser = userInfoRepository.findByUserNameAndStatus(userName, Status.ACTIVE);
		if (existingUser == null) {
			throw new UserNotFoundException("User inctive/doesnot exists in the system");
		}
		return existingUser;
	}

	public boolean activateUser(String userName, String otp) {
		final UserInfo userInfo = validateInactiveSystemUserByOtp(userName, otp);
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

	private UserInfoRespone pageableEventResponse(Page<UserInfo> pagedEventList, Specification<UserInfo> spec,
			int currentPage) {
		final GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		final UserInfoRespone eventDtoList = new UserInfoRespone();
		final ColumnConfigDto columnConfig = new ColumnConfigDto(TableConfig.USER_COlUMNS);
		eventDtoList.setColumnConfig(columnConfig);
		eventDtoList.setUserInfoList(mapper.entityToDto(pagedEventList.getContent()));
		eventDtoList.setUserPage(buildEventPage(spec, currentPage, pagedEventList.getContent().size()));
		return eventDtoList;
	}

	private UserPage buildEventPage(Specification<UserInfo> spec, int currentPage, int currentRecordSize) {
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
		eventPage
				.setDisplayPagesIndex(generateDisplayPagesIndex(eventPage.getTotalPages(), eventPage.getCurrentPage()));

		return eventPage;
	}

	private List<Integer> generateDisplayPagesIndex(int totalPages, int currentPage) {
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

	private int getEventCount(Specification<UserInfo> spec) {
		return spec != null ? (int) userInfoRepository.count(spec) : (int) userInfoRepository.count();
	}

}
