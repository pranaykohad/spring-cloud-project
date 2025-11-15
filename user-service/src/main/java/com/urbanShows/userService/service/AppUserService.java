package com.urbanShows.userService.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.dto.AppUserDto;
import com.urbanShows.userService.dto.AppUserRegisterDto;
import com.urbanShows.userService.entity.AppUser;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;
import com.urbanShows.userService.exception.AccessDeniedException;
import com.urbanShows.userService.exception.UserAlreadyExistsException;
import com.urbanShows.userService.exception.UserNotFoundException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.repository.AppUserRepository;
import com.urbanShows.userService.util.AuthTokenAndPasswordUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AppUserService {

	private final ModelMapper modelMapper;
	private final AppUserRepository appUserInfoRepo;
	private final OtpService otpService;

	public Boolean registerAppUser(AppUserRegisterDto appUserDto) {
		if (appUserInfoRepo.existsById(appUserDto.getPhone())) {
			log.error("App User with phone {} already exists in the system ", appUserDto.getPhone());
			throw new UserAlreadyExistsException("User already exists in the system");
		}
		final GenericMapper<AppUserRegisterDto, AppUser> mapper = new GenericMapper<>(modelMapper,
				AppUserRegisterDto.class, AppUser.class);
		final AppUser appUser = mapper.dtoToEntity(appUserDto);
		appUser.setRoles(List.of(Role.APP_USER));
		appUser.setInternalPassword(AuthTokenAndPasswordUtil.generatorPassword());
		appUser.setStatus(Status.ACTIVE);
		appUserInfoRepo.save(appUser);
		otpService.createOtpForAppUser(appUser);
		log.info("App User {} with phone is register in the system ", appUserDto.getDisplayName(),
				appUserDto.getPhone());
		return true;
	}

	public void uploadAppUserProfile(AppUser appUser, String profilePicUrl) {
		appUser.setProfilePicUrl(profilePicUrl);
		appUserInfoRepo.save(appUser);
	}

	public AppUserDto editAppUser(AppUser appUser, AppUserDto newAppUserDto) {
		appUser.setDisplayName(newAppUserDto.getDisplayName() != null
				&& !appUser.getDisplayName().equals(newAppUserDto.getDisplayName()) ? newAppUserDto.getDisplayName()
						: appUser.getDisplayName());
		appUser.setEmail(newAppUserDto.getEmail() != null && !appUser.getEmail().equals(newAppUserDto.getEmail())
				? newAppUserDto.getEmail()
				: appUser.getEmail());
		final AppUser save = appUserInfoRepo.save(appUser);
		final GenericMapper<AppUserDto, AppUser> mapper = new GenericMapper<>(modelMapper, AppUserDto.class,
				AppUser.class);
		return mapper.entityToDto(save);
	}

	public AppUser getExistingAppUser(String phoneNumber) {
		final AppUser existingAppUser = appUserInfoRepo.findByPhone(phoneNumber);
		if (existingAppUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingAppUser;
	}

	public List<AppUserDto> getAppUserList() {
		final List<AppUser> appUserList = appUserInfoRepo.findAll();
		final GenericMapper<AppUserDto, AppUser> mapper = new GenericMapper<>(modelMapper, AppUserDto.class,
				AppUser.class);
		return mapper.entityToDto(appUserList);
	}
	
	public AppUser authenticateAppUserByOtp(String phone, String otp) {
		final AppUser appUser = appUserInfoRepo.findByPhoneAndOtp(phone, otp);
		if (appUser == null) {
			throw new AccessDeniedException("Phone number or OTP is not correct");
		}
		return appUser;
	}

	public void generateOtp(String phone) {
		log.info("Generating OTP for App User with phone {}", phone);
		final AppUser existingAppUser = getExistingAppUser(phone);
		otpService.createOtpForAppUser(existingAppUser);
		log.info("OTP generated and sent to App User with phone {}", phone);
	}

	@Transactional
	public void deleteAppUser(AppUserDto appUser) {
		// TODO: need soft delete, to be implemented, active -> inactive
		final GenericMapper<AppUserDto, AppUser> mapper = new GenericMapper<>(modelMapper, AppUserDto.class,
				AppUser.class);
		appUserInfoRepo.delete(mapper.dtoToEntity(appUser));
	}

}
