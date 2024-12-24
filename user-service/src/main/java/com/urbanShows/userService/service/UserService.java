package com.urbanShows.userService.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

	public boolean udpateSecuredUserDetails(UserSecuredDetailsReq securedDetails, UserInfo existingSystemUser) {
		final GenericMapper<UserSecuredDetailsReq, UserInfoDto> mapper = new GenericMapper<>(modelMapper,
				UserSecuredDetailsReq.class, UserInfoDto.class);
		UserInfoDto dtoToEntity = mapper.dtoToEntity(securedDetails);
		updateSecuredUserDetails(dtoToEntity, existingSystemUser);
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

	public boolean udpateBasicUserDetails(UserBasicDetails basicDetails, UserInfo systemUser) {
		systemUser.setDisplayName(StringUtils.hasText(basicDetails.getDisplayName())
				&& !systemUser.getDisplayName().equals(basicDetails.getDisplayName())
						? basicDetails.getDisplayName().trim()
						: systemUser.getDisplayName());

		if (basicDetails.getProfilePicFile() != null) {
			final String fileUrl = azureBlobStorageService.uploadSystemUserProfile(basicDetails.getProfilePicFile(),
					systemUser);
			uploadSystemUserProfilePicUrl(systemUser, fileUrl);
		}

		systemUserRepo.save(systemUser);
		return true;
	}

	private void updateSecuredUserDetails(UserInfoDto targetUserDto, UserInfo systemUser) {
		if (!targetUserDto.getPassword().isEmpty()
				&& !passwordEncoder.matches(targetUserDto.getPassword(), systemUser.getPassword())) {
			systemUser.setPassword(passwordEncoder.encode(targetUserDto.getPassword()));
		}
		systemUser
				.setPhone(!targetUserDto.getPhone().isEmpty() && !systemUser.getPhone().equals(targetUserDto.getPhone())
						? targetUserDto.getPhone()
						: systemUser.getPhone());
		systemUser
				.setEmail(!targetUserDto.getEmail().isEmpty() && !systemUser.getEmail().equals(targetUserDto.getEmail())
						? targetUserDto.getEmail()
						: systemUser.getEmail());
		systemUserRepo.save(systemUser);
	}

	public UserInfo isUserActive(String userName) {
		final UserInfo existingSystemUser = getExistingSystemUser(userName);
		if (existingSystemUser.getStatus().equals(Status.ACTIVE)) {
			return existingSystemUser;
		} else {
			throw new UserInactiveException("The user is not active. Please contact the supervisor for assistance");
		}
	}

	private UserInfo getExistingSystemUser(String userName) {
		final UserInfo existingUser = systemUserRepo.findByUserName(userName);
		if (existingUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingUser;
	}

}
