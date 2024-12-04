package com.urbanShows.userService.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.AppUserSigninReqDto;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.exceptionHandler.AccessDeniedException;
import com.urbanShows.userService.exceptionHandler.UserAlreadyExistsException;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.kafka.OtpkafkaDto;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.repository.AppUserInfoRepository;
import com.urbanShows.userService.util.AuthTokenAndPasswordUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserService {

	private final ModelMapper modelMapper;
	private final AppUserInfoRepository appUserInfoRepo;
	private final MessageProducer messageProducer;

	public Boolean registerAppUser(AppUserSigninReqDto appUserDto) {
		if (!appUserDto.getRoles().contains(Role.APP_USER) || appUserDto.getDisplayName() == null
				|| (appUserDto.getDisplayName() != null && appUserDto.getDisplayName().isEmpty())) {
			throw new AccessDeniedException("You are not authorized to do this operation");
		}
		final AppUserInfo existingAppUser = appUserInfoRepo.findByPhone(appUserDto.getPhone());
		if (existingAppUser != null) {
			throw new UserAlreadyExistsException("User already exists in the system");
		}
		final GenericMapper<AppUserSigninReqDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper,
				AppUserSigninReqDto.class, AppUserInfo.class);
		final AppUserInfo appUser = mapper.dtoToEntity(appUserDto);
		appUser.setInternalPassword(AuthTokenAndPasswordUtil.generatorPassword());
		appUser.setOtp(AuthTokenAndPasswordUtil.generateAuthToken());
		appUser.setOtpTimeStamp(LocalDateTime.now());
		appUserInfoRepo.save(appUser);
		final OtpkafkaDto otpkafkaDto = new OtpkafkaDto(appUser.getPhone(), appUser.getOtp());
		messageProducer.sendOtpMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), otpkafkaDto);
		log.info("App User {} with phone is register in the system ", appUserDto.getDisplayName(),
				appUserDto.getPhone());
		return true;
	}

	@Transactional
	public void deleteAppUser(AppUserInfoDto appUser) {
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		appUserInfoRepo.delete(mapper.dtoToEntity(appUser));
	}

	public AppUserInfoDto getAppUserByName(String phone) {
		final AppUserInfo appUser = appUserInfoRepo.findByPhone(phone);
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(appUser);
	}

	public List<AppUserInfoDto> getBackendUsersList() {
		final List<AppUserInfo> all = appUserInfoRepo.findAll();
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(all);
	}

	public void uploadProfilePic(AppUserInfoDto appUserDto, String profilePicUrl) {
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		final AppUserInfo appUser = mapper.dtoToEntity(appUserDto);
		appUser.setProfilePicUrl(profilePicUrl);
		appUserInfoRepo.save(appUser);
	}

	public AppUserInfoDto isPhoneNumberExists(String phoneNumber) {
		final AppUserInfo existingAppUser = isAppUserExists(phoneNumber);
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(existingAppUser);
	}

	public AppUserInfoDto udpate(AppUserInfoDto appUserDto) {
		final AppUserInfo appUser = appUserInfoRepo.findByPhone(appUserDto.getPhone());
		if (appUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		final AppUserInfo save = appUserInfoRepo.save(mapper.dtoToEntity(appUserDto));
		return mapper.entityToDto(save);
	}

	public AppUserInfoDto getAppUserByPhone(String phone) {
		final AppUserInfo appUser = isAppUserExists(phone);
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(appUser);
	}

	public AppUserInfo isAppUserExists(String phoneNumber) {
		final AppUserInfo existingAppUser = appUserInfoRepo.findByPhone(phoneNumber);
		if (existingAppUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingAppUser;
	}

	public AppUserInfoDto authenticateAppUser(AppUserInfoDto appUserDto) {
		verifyAppUserRole(appUserDto);
		final AppUserInfo appUser = appUserInfoRepo.findByPhoneAndOtp(appUserDto.getPhone(), appUserDto.getOtp());
		if (appUser == null) {
			throw new AccessDeniedException("You are not authorized to do this operation");
		}
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(appUser);
	}

	private void verifyAppUserRole(AppUserInfoDto appUserDto) {
		if (appUserDto.getRoles() == null || (appUserDto.getRoles() != null
				&& (appUserDto.getRoles().isEmpty() || !appUserDto.getRoles().contains(Role.APP_USER)))) {
			throw new AccessDeniedException("You are not authorized to do this operation");
		}
	}

}
