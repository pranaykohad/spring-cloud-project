package com.urbanShows.userService.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.dto.UserSigninDto;
import com.urbanShows.userService.dto.TargetUserDto;
import com.urbanShows.userService.dto.UserUpdateDto;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.exceptionHandler.AccessDeniedException;
import com.urbanShows.userService.exceptionHandler.UserAlreadyExistsException;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
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
	private final AppUserService appUserService;

	public void uploadProfilePicUrl(UserInfoDto systemUserDto, String profilePicUrl) {
		final UserInfo appUser = new UserInfo();
		appUser.setUserName(systemUserDto.getUserName());
		appUser.setProfilePicUrl(profilePicUrl);
		systemUserRepo.save(appUser);
	}

	public UserInfo authenticateSystemUserByOtp(String userName, String otp) {
		final UserInfo systemUser = systemUserRepo.findByUserNameAndOtp(userName, otp);
		if (systemUser == null) {
			throw new AccessDeniedException("Username or OTP is not correct");
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
		final GenericMapper<UserSigninDto, UserInfo> mapper = new GenericMapper<>(modelMapper,
				UserSigninDto.class, UserInfo.class);
		final UserInfo systemUser = mapper.dtoToEntity(systemUserSigninDto);
		final List<Role> roleList = new ArrayList<>();
		roleList.addAll(systemUserSigninDto.getRoles());
		systemUser.setRoles(roleList);
		systemUser.setPassword(passwordEncoder.encode(systemUserSigninDto.getPassword()));
		systemUserRepo.save(systemUser);
		log.info("System User {} is register in the system ", systemUserSigninDto.getUserName());
		return true;
	}

	public void uploadProfilePic(UserInfo systemUser, String profilePicUrl) {
		systemUser.setProfilePicUrl(profilePicUrl);
		systemUserRepo.save(systemUser);
	}

	public UserInfo getExistingSystemUser(String userName) {
		final UserInfo existingUser = systemUserRepo.findByUserName(userName);
		if (existingUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingUser;
	}

	public List<UserInfoDto> getSystemUsersList() {
		final GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper,
				UserInfoDto.class, UserInfo.class);
		final List<UserInfo> all = systemUserRepo.findAll();
		return mapper.entityToDto(all);
	}

	@Transactional
	public void deleteSystemUserByUserName(UserInfoDto systemUserDto) {
		final GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper,
				UserInfoDto.class, UserInfo.class);
		systemUserRepo.delete(mapper.dtoToEntity(systemUserDto));
	}

	public boolean udpateUserDetails(UserUpdateDto userUpdateDto) {
		final TargetUserDto targetUserDto = userUpdateDto.getTargetUserDto();
		if (targetUserDto.getAppUserInfoDto() != null) {
			updateAppUserDetails(targetUserDto.getAppUserInfoDto());
		} else if (targetUserDto.getSystemUserInfoDto() != null) {
			updateSystemUserDetails(targetUserDto.getSystemUserInfoDto());
		}
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

	private void updateAppUserDetails(AppUserInfoDto newAppUserDto) {
		final AppUserInfo appUser = appUserService.getExistingAppUser(newAppUserDto.getPhone());
		appUserService.udpate(appUser, newAppUserDto);
	}

	private void updateSystemUserDetails(UserInfoDto targetUserDto) {
		final UserInfo systemUser = getExistingSystemUser(targetUserDto.getUserName());
		systemUser.setPassword(targetUserDto.getPassword() != null
				&& !passwordEncoder.matches(targetUserDto.getPassword(), systemUser.getPassword())
						? passwordEncoder.encode(targetUserDto.getPassword())
						: systemUser.getPassword());
		systemUser.setDisplayName(targetUserDto.getDisplayName() != null
				&& !systemUser.getDisplayName().equals(targetUserDto.getDisplayName()) ? targetUserDto.getDisplayName()
						: systemUser.getDisplayName());
		systemUser.setPhone(targetUserDto.getPhone() != null && !systemUser.getPhone().equals(targetUserDto.getPhone())
				? targetUserDto.getPhone()
				: systemUser.getPhone());
		systemUser.setEmail(targetUserDto.getEmail() != null && !systemUser.getEmail().equals(targetUserDto.getEmail())
				? targetUserDto.getEmail()
				: systemUser.getEmail());
		systemUserRepo.save(systemUser);
	}

}
