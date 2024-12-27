package com.urbanShows.userService.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.constants.ColumnConfigList;
import com.urbanShows.userService.dto.ColumnConfig;
import com.urbanShows.userService.dto.UserBasicDetails;
import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.dto.UserInfoListDto;
import com.urbanShows.userService.dto.UserSecuredDetailsReq;
import com.urbanShows.userService.dto.UserSigninDto;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.entity.RolePriority;
import com.urbanShows.userService.entity.Status;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.exception.AccessDeniedException;
import com.urbanShows.userService.exception.IncorrectOtpException;
import com.urbanShows.userService.exception.UserAlreadyExistsException;
import com.urbanShows.userService.exception.UserInactiveException;
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

	private final UserInfoRepository systemUserRepo;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final MessageProducer messageProducer;
	private final OtpService otpService;
	private final AzureBlobStorageService azureBlobStorageService;

	public void uploadProfilePicUrl(UserInfoDto systemUserDto, String profilePicUrl) {
		final UserInfo appUser = new UserInfo();
		appUser.setUserName(systemUserDto.getUserName());
		appUser.setProfilePicUrl(profilePicUrl);
		systemUserRepo.save(appUser);
	}

	public UserInfo authenticateSystemUserByOtp(String userName, String otp) {
		final UserInfo systemUser = systemUserRepo.findByUserNameAndOtp(userName, otp);
		if (systemUser == null) {
			throw new IncorrectOtpException("OTP is not correct or expired");
		}
		return systemUser;
	}

	public UserInfo authenticateSystemUserByPassword(String userName, String password) {
		final UserInfo systemUser = systemUserRepo.findByUserNameAndPassword(userName,
				passwordEncoder.encode(password.trim()));
		if (systemUser == null) {
			throw new AccessDeniedException("Username or password is not correct");
		}
		return systemUser;
	}

	public void uploadSystemUserProfilePicUrl(UserInfo systemUser, String profilePicUrl) {
		systemUser.setProfilePicUrl(profilePicUrl);
		systemUserRepo.save(systemUser);
	}

	public Boolean addSystemUser(UserSigninDto systemUserSigninDto) {
		if (systemUserRepo.existsById(systemUserSigninDto.getUserName())) {
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
		roleList.stream().filter(i -> i.equals(Role.SUPER_ADMIN_USER))
				.forEach(i -> systemUser.setStatus(Status.ACTIVE));
		systemUser.setCreatedAt(LocalDateTime.now());
		systemUserRepo.save(systemUser);
		log.info("System User {} is register in the system ", systemUserSigninDto.getUserName());
		return true;
	}

	public UserInfoListDto getSystemUsersList() {
		final GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);

		final long count = systemUserRepo.count();
		final ColumnConfig columnConfig = new ColumnConfig();
		columnConfig.setColumns(ColumnConfigList.USER_COlUMNS);
		columnConfig.setRowsPerPage(10);
		columnConfig.setTotalRows(count);

		final List<UserInfo> all = systemUserRepo.findAll();
		final UserInfoListDto userInfoListDto = new UserInfoListDto();
		userInfoListDto.setUserInfoList(mapper.entityToDto(all));
		userInfoListDto.setColumnConfig(columnConfig);

		return userInfoListDto;
	}

	@Transactional
	public void deleteSystemUserByUserName(UserInfoDto systemUserDto) {
		final GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		systemUserRepo.delete(mapper.dtoToEntity(systemUserDto));
	}

	public boolean udpateSecuredUserDetails(UserSecuredDetailsReq securedDetailsReq, UserInfo targetUser, String loggedInUserName) {
		final GenericMapper<UserSecuredDetailsReq, UserInfoDto> mapper = new GenericMapper<>(modelMapper,
				UserSecuredDetailsReq.class, UserInfoDto.class);
		final UserInfoDto dtoToEntity = mapper.dtoToEntity(securedDetailsReq);
		updateSecuredUserDetails(dtoToEntity, targetUser, loggedInUserName);
		return true;
	}

	public void generateOtpForSystemUser(String userName) {
		final UserInfo systemUser = getExistingSystemUser(userName);
		otpService.createOtpForSystemUser(systemUser);
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
			final String fileUrl = azureBlobStorageService.uploadSystemUserProfile(basicDetails.getProfilePicFile(),
					targetUser);
			uploadSystemUserProfilePicUrl(targetUser, fileUrl);
		}

		systemUserRepo.save(targetUser);
		return true;
	}

	private void updateSecuredUserDetails(UserInfoDto newUserInfo, UserInfo targetUserInfo, String loggedInUserName) {
		if (StringUtils.hasText(newUserInfo.getPassword())) {
			targetUserInfo.setPassword(passwordEncoder.encode(newUserInfo.getPassword()));
		}
		targetUserInfo.setPhone(
				StringUtils.hasText(newUserInfo.getPhone()) && !targetUserInfo.getPhone().equals(newUserInfo.getPhone())
						? newUserInfo.getPhone()
						: targetUserInfo.getPhone());
		targetUserInfo.setEmail(
				StringUtils.hasText(newUserInfo.getEmail()) && !targetUserInfo.getEmail().equals(newUserInfo.getEmail())
						? newUserInfo.getEmail()
						: targetUserInfo.getEmail());
		if (!loggedInUserName.equals(targetUserInfo.getUserName())) {
			targetUserInfo
					.setStatus(!newUserInfo.getStatus().equals(targetUserInfo.getStatus()) ? newUserInfo.getStatus()
							: targetUserInfo.getStatus());
		}
		systemUserRepo.save(targetUserInfo);
	}

	public UserInfo isUserActive(String userName) {
		final UserInfo existingSystemUser = getExistingSystemUser(userName);
		if (existingSystemUser.getStatus().equals(Status.ACTIVE)) {
			return existingSystemUser;
		} else {
			throw new UserInactiveException("The user is not active. Please contact the supervisor for assistance");
		}
	}

	public UserInfo getExistingSystemUser(String userName) {
		final UserInfo existingUser = systemUserRepo.findByUserName(userName);
		if (existingUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingUser;
	}

//	public UserInfo isPermitted(UserInfo currentUser, UserInfo targetUser) {
//		final Role role = currentUser.getRoles().get(0);
//		return true;
//	}

}
