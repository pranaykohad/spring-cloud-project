package com.urbanShows.userService.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.AppUserSigninReqDto;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;
import com.urbanShows.userService.exception.AccessDeniedException;
import com.urbanShows.userService.exception.UserAlreadyExistsException;
import com.urbanShows.userService.exception.UserInactiveException;
import com.urbanShows.userService.exception.UserNotFoundException;
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
	private final OtpService otpService;

	public Boolean signinAppUser(AppUserSigninReqDto appUserDto) {
		if (appUserInfoRepo.existsById(appUserDto.getPhone())) {
			throw new UserAlreadyExistsException("User already exists in the system");
		}
		final GenericMapper<AppUserSigninReqDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper,
				AppUserSigninReqDto.class, AppUserInfo.class);
		final AppUserInfo appUser = mapper.dtoToEntity(appUserDto);
		appUser.setRoles(List.of(Role.APP_USER));
		appUser.setInternalPassword(AuthTokenAndPasswordUtil.generatorPassword());
		appUserInfoRepo.save(appUser);
		otpService.createOtpForAppUser(appUser);
		log.info("App User {} with phone is register in the system ", appUserDto.getDisplayName(),
				appUserDto.getPhone());
		return true;
	}

	public void generateOtpForAppUser(String phone) {
		final AppUserInfo existingAppUser = getExistingAppUser(phone);
		otpService.createOtpForAppUser(existingAppUser);
	}

	@Transactional
	public void deleteAppUser(AppUserInfoDto appUser) {
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		appUserInfoRepo.delete(mapper.dtoToEntity(appUser));
	}

	public List<AppUserInfoDto> getBackendUsersList() {
		final List<AppUserInfo> all = appUserInfoRepo.findAll();
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(all);
	}

	public void uploadAppUserProfilePicUrl(AppUserInfo appUser, String profilePicUrl) {
		appUser.setProfilePicUrl(profilePicUrl);
		appUserInfoRepo.save(appUser);
	}

	public AppUserInfoDto udpate(AppUserInfo appUser, AppUserInfoDto newAppUserDto) {
		appUser.setDisplayName(newAppUserDto.getDisplayName() != null
				&& !appUser.getDisplayName().equals(newAppUserDto.getDisplayName()) ? newAppUserDto.getDisplayName()
						: appUser.getDisplayName());
		appUser.setEmail(newAppUserDto.getEmail() != null && !appUser.getEmail().equals(newAppUserDto.getEmail())
				? newAppUserDto.getEmail()
				: appUser.getEmail());
		final AppUserInfo save = appUserInfoRepo.save(appUser);
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(save);
	}

	public AppUserInfo getExistingAppUser(String phoneNumber) {
		final AppUserInfo existingAppUser = appUserInfoRepo.findByPhone(phoneNumber);
		if (existingAppUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingAppUser;
	}

	public AppUserInfo authenticateAppUserByOtp(String phone, String otp) {
		final AppUserInfo appUser = appUserInfoRepo.findByPhoneAndOtp(phone, otp);
		if (appUser == null) {
			throw new AccessDeniedException("Phone number or OTP is not correct");
		}
		return appUser;
	}

	public List<AppUserInfoDto> getUserList() {
		final List<AppUserInfo> appUserList = appUserInfoRepo.findAll();
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(appUserList);
	}

	public void updateAppUserDetails(AppUserInfoDto appUserInfoDto) {
		getExistingAppUser(appUserInfoDto.getPhone());
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		appUserInfoRepo.save(mapper.dtoToEntity(appUserInfoDto));
	}

}
