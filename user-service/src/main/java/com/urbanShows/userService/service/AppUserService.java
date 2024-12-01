package com.urbanShows.userService.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.exceptionHandler.UnauthorizedException;
import com.urbanShows.userService.exceptionHandler.UserAlreadyExistsException;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.repository.AppUserInfoRepository;
import com.urbanShows.userService.util.Helper;
import com.urbanShows.userService.util.OTPGenerator;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService {

	private ModelMapper modelMapper;
	private AppUserInfoRepository appUserInfoRepo;

	public Boolean addAppUser(AppUserInfoDto appUserIntoDto) {
		if (appUserIntoDto.getRoles().contains(Role.APP_USER) && appUserIntoDto.getDisplayName() != null
				&& !appUserIntoDto.getDisplayName().isEmpty()) {
			AppUserInfo existingAppUser = appUserInfoRepo.findByPhone(appUserIntoDto.getPhone());
			if (existingAppUser != null) {
				throw new UserAlreadyExistsException("This Phone number is already registred");
			}
			GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
					AppUserInfo.class);
			AppUserInfo finalAppUser = mapper.dtoToEntity(appUserIntoDto);
			finalAppUser.setInternalPassword(OTPGenerator.generatorPassword());
			String otp = OTPGenerator.generateOTP();
			finalAppUser.setOtp(otp);
			finalAppUser.setOtpDateTime(LocalDateTime.now());
			appUserInfoRepo.save(finalAppUser);
			// send otp on user's phone
			return true;
		} else {
			throw new UnauthorizedException("Invalid input paramaters");
		}
	}

	public String generateAndSaveOtp(AppUserInfoDto appUserDto) {
		GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		AppUserInfo finalAppUser = mapper.dtoToEntity(appUserDto);
		String otp = OTPGenerator.generateOTP();
		finalAppUser.setOtp(otp);
		finalAppUser.setOtpDateTime(LocalDateTime.now());
		appUserInfoRepo.save(finalAppUser);
		// send otp on user's phone
		return otp;
	}

	public AppUserInfoDto getAppUserByName(String phone) {
		AppUserInfo byName = appUserInfoRepo.findByPhone(phone);
		GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return mapper.entityToDto(byName);
	}

	public List<AppUserInfoDto> getBackendUsersList() {
		GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		List<AppUserInfo> all = appUserInfoRepo.findAll();
		return mapper.entityToDto(all);
	}

	public AppUserInfoDto udpate(AppUserInfoDto appUserInfoDto) {
		AppUserInfo existingAppUser = appUserInfoRepo.findByPhone(appUserInfoDto.getPhone());
		if (existingAppUser == null) {
			throw new UserNotFoundException("This Phone does not exists in system");
		}
		GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		AppUserInfo save = appUserInfoRepo.save(mapper.dtoToEntity(appUserInfoDto));
		return mapper.entityToDto(save);
	}

	public boolean verifyOtp(AppUserInfoDto userInfo) {
		AppUserInfo existingAppUser = appUserInfoRepo.findByPhoneAndOtp(userInfo.getPhone(), userInfo.getOtp());
		return existingAppUser != null;
	}

	public AppUserInfoDto getAppUserByPhone(String phone) {
		AppUserInfo existingAppUser = appUserInfoRepo.findByPhone(phone);
		if (existingAppUser == null) {
			throw new UserNotFoundException("This Phone does not exists in system");
		}
		GenericMapper<AppUserInfo, AppUserInfoDto> mapper = new GenericMapper<>(modelMapper, AppUserInfo.class,
				AppUserInfoDto.class);
		return mapper.dtoToEntity(existingAppUser);
	}

	@Transactional
	public void deleteUserByPhone(String phone) {
		appUserInfoRepo.deleteByPhone(phone);
	}

}
