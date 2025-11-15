package com.urbanShows.userService.controller.appUser;

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

import com.urbanShows.userService.aws.AwsS3Service;
//import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.dto.AppUserDto;
import com.urbanShows.userService.entity.AppUser;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.AppUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/app")
@AllArgsConstructor
public class AppUserController {

	private final AppUserService appUserService;
	private final AwsS3Service awsS3Service;
	private final ModelMapper modelMapper;

	@GetMapping
	public ResponseEntity<AppUserDto> getAppUser(@RequestParam String phone) {
		final AppUser existingAppUser = appUserService.getExistingAppUser(phone);
		final GenericMapper<AppUserDto, AppUser> mapper = new GenericMapper<>(modelMapper, AppUserDto.class,
				AppUser.class);
		return ResponseEntity.ok(mapper.entityToDto(existingAppUser));
	}

	@PatchMapping("edit")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public ResponseEntity<AppUserDto> editAppUser(@Valid @RequestBody AppUserDto appUserDto) {
		final AppUser appUser = appUserService.authenticateAppUserByOtp(appUserDto.getPhone(), appUserDto.getOtp());
		return ResponseEntity.ok(appUserService.editAppUser(appUser, appUserDto));
	}

	@DeleteMapping("delete")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public ResponseEntity<Boolean> deleteAppUser(@Valid @RequestBody AppUserDto appUserDto) {
		appUserService.authenticateAppUserByOtp(appUserDto.getPhone(), appUserDto.getOtp());
		appUserService.deleteAppUser(appUserDto);
		return ResponseEntity.ok(true);
	}

	@GetMapping("generate-otp")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public void generateOtp(@RequestParam String phone) {
		appUserService.generateOtp(phone);
	}

	@PatchMapping("update-profile-pic")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public ResponseEntity<Boolean> uploadAppUserProfilePic(@RequestParam MultipartFile file,
			@RequestPart String phone, @RequestPart String otp) {
		final AppUser appUser = appUserService.authenticateAppUserByOtp(phone, otp);
		String url = awsS3Service.uploadFile(file);
		appUserService.uploadAppUserProfile(appUser, url);
		return ResponseEntity.ok(true);
	}

}
