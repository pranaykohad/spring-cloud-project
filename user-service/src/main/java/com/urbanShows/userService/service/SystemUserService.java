package com.urbanShows.userService.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.dto.TargetUserDto;
import com.urbanShows.userService.dto.UserUpdateDto;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.exceptionHandler.AccessDeniedException;
import com.urbanShows.userService.exceptionHandler.UserAlreadyExistsException;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.kafka.OtpkafkaDto;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.repository.SystemUserInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SystemUserService {

	private final SystemUserInfoRepository userRepo;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final MessageProducer messageProducer;
	private final OtpService otpService;
	private final AppUserService appUserService;

	public void uploadProfilePicUrl(SystemUserInfoDto systemUserDto, String profilePicUrl) {
		final SystemUserInfo appUser = new SystemUserInfo();
		appUser.setUserName(systemUserDto.getUserName());
		appUser.setProfilePicUrl(profilePicUrl);
		userRepo.save(appUser);
	}

	public SystemUserInfoDto authenticateSystemUserByOtp(String userName, String otp) {
//		verifySystemUserRole(systemUserDto);
		final SystemUserInfo systemUser = userRepo.findByUserNameAndOtp(userName, otp);
		if (systemUser == null) {
			throw new AccessDeniedException("Username or OTP is not correct");
		}
		final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		return mapper.entityToDto(systemUser);
	}

	public Boolean addSystemUser(SystemUserInfoDto systemUserDto) {
		if (systemUserDto.getRoles().contains(Role.SYSTEM_USER)) {
			systemUserDto.setPassword(passwordEncoder.encode(systemUserDto.getPassword()));
			final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
					SystemUserInfoDto.class, SystemUserInfo.class);
			final SystemUserInfo systemUser = mapper.dtoToEntity(systemUserDto);
			final Optional<SystemUserInfo> existingUser = userRepo.findById(systemUser.getUserName());
			if (existingUser.isPresent()) {
				throw new UserAlreadyExistsException("User already exists in the system");
			}
			userRepo.save(systemUser);
			return true;
		} else {
			throw new AccessDeniedException("You are not authorized to do this operation");
		}
	}

	public void uploadProfilePic(SystemUserInfo systemUser, String profilePicUrl) {
		systemUser.setProfilePicUrl(profilePicUrl);
		userRepo.save(systemUser);
	}

	public SystemUserInfo getSystemUserByUserName(String userName) {
		final SystemUserInfo existingUser = userRepo.findByUserName(userName);
		if (existingUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingUser;
	}

	public List<SystemUserInfoDto> getSystemUsersList() {
		final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		final List<SystemUserInfo> all = userRepo.findAll();
		return mapper.entityToDto(all);
	}

	@Transactional
	public void deleteSystemUserByUserName(SystemUserInfoDto systemUserDto) {
		final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		userRepo.delete(mapper.dtoToEntity(systemUserDto));
	}

	public boolean udpateUserDetails(UserUpdateDto userUpdateDto) {
		final TargetUserDto targetUserDto = userUpdateDto.getTargetUserDto();
		if (targetUserDto.getAppUserInfoDto() != null) {
			updateAppUserDetails(targetUserDto);
		} else if (targetUserDto.getSystemUserInfoDto() != null) {
			updateSystemUserDetails(targetUserDto);
		}
		return true;
	}

	private void updateSystemUserDetails(TargetUserDto targetUserDto) {
		final SystemUserInfo existingSystemUser = getSystemUserByUserName(
				targetUserDto.getSystemUserInfoDto().getUserName());
		final SystemUserInfo newSystemUser = new SystemUserInfo();
		existingSystemUser
				.setEmail(!existingSystemUser.getEmail().equals(newSystemUser.getEmail()) ? newSystemUser.getEmail()
						: existingSystemUser.getEmail());
		existingSystemUser.setPassword(
				!existingSystemUser.getPassword().equals(newSystemUser.getPassword()) ? newSystemUser.getPassword()
						: existingSystemUser.getPassword());
		existingSystemUser.setDisplayName(!existingSystemUser.getDisplayName().equals(newSystemUser.getDisplayName())
				? newSystemUser.getDisplayName()
				: existingSystemUser.getDisplayName());
		existingSystemUser
				.setPhone(!existingSystemUser.getPhone().equals(newSystemUser.getPhone()) ? newSystemUser.getPhone()
						: existingSystemUser.getPhone());
		updateSystemUserdetails(targetUserDto.getSystemUserInfoDto());
	}

	private void updateAppUserDetails(TargetUserDto targetUserDto) {
		final AppUserInfoDto existingAppUser = appUserService
				.getAppUserByPhone(targetUserDto.getAppUserInfoDto().getPhone());
		final AppUserInfoDto newAppUser = new AppUserInfoDto();
		newAppUser.setPhone(!existingAppUser.getPhone().equals(newAppUser.getPhone()) ? newAppUser.getPhone()
				: existingAppUser.getPhone());
		newAppUser.setDisplayName(
				!existingAppUser.getDisplayName().equals(newAppUser.getDisplayName()) ? newAppUser.getDisplayName()
						: existingAppUser.getDisplayName());
		newAppUser.setEmail(!existingAppUser.getEmail().equals(newAppUser.getEmail()) ? newAppUser.getEmail()
				: existingAppUser.getEmail());
		appUserService.updateAppUserDetails(targetUserDto.getAppUserInfoDto());
	}

	private void updateSystemUserdetails(SystemUserInfoDto systemUser) {
		getSystemUserByUserName(systemUser.getUserName());
		final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		userRepo.save(mapper.dtoToEntity(systemUser));
	}

	public void generateOtpForSystemUser(String userName) {
		final SystemUserInfo systemUser = getSystemUserByUserName(userName);
		otpService.createOtpForSystemUser(systemUser);
	}

	public void sendOtpToPhone(String phone, String otp) {
		final OtpkafkaDto otpkafkaDto = new OtpkafkaDto(phone, otp);
		messageProducer.sendOtpMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), otpkafkaDto);
	}

	private void verifySystemUserRole(SystemUserInfoDto systemUser) {
		if (systemUser.getRoles() == null || (systemUser.getRoles() != null
				&& (systemUser.getRoles().isEmpty() || !systemUser.getRoles().contains(Role.SYSTEM_USER)))) {
			throw new AccessDeniedException("You are not authorized to do this operation");
		}
	}

}
