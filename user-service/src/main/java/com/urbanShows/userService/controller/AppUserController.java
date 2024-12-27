package com.urbanShows.userService.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.AppUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/app")
@AllArgsConstructor
public class AppUserController {

	private final AppUserService appUserService;
	private final AzureBlobStorageService azureBlobStorageService;
	private final ModelMapper modelMapper;

	@GetMapping
	public ResponseEntity<AppUserInfoDto> getAppUser(@RequestParam String phone) {
		final AppUserInfo existingAppUser = appUserService.getExistingAppUser(phone);
		final GenericMapper<AppUserInfoDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper, AppUserInfoDto.class,
				AppUserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingAppUser));
	}

	@PatchMapping("udpate")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public ResponseEntity<AppUserInfoDto> udpateUser(@Valid @RequestBody AppUserInfoDto appUserDto) {
		final AppUserInfo appUser = appUserService.authenticateAppUserByOtp(appUserDto.getPhone(), appUserDto.getOtp());
		return ResponseEntity.ok(appUserService.udpate(appUser, appUserDto));
	}

	@DeleteMapping("remove")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody AppUserInfoDto appUserDto) {
		appUserService.authenticateAppUserByOtp(appUserDto.getPhone(), appUserDto.getOtp());
		appUserService.deleteAppUser(appUserDto);
		return ResponseEntity.ok(true);
	}

	@GetMapping("generate-otp")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public void generateOtp(@RequestParam String phone) {
		appUserService.generateOtpForAppUser(phone);
	}
//
//	@PatchMapping("update-profile-pic")
//	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
//	public ResponseEntity<Boolean> uploadAppUserProfilePic(@RequestParam MultipartFile file,
//			@RequestPart String phone, @RequestPart String otp) {
//		final AppUserInfo appUser = appUserService.authenticateAppUserByOtp(phone, otp);
//		boolean uploadAppUserProfile = azureBlobStorageService.uploadAppUserProfile(file, appUser);
//		return ResponseEntity.ok(uploadAppUserProfile);
//	}

}
