package com.urbanShows.userService.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.dto.SystemUserResponseDto;
import com.urbanShows.userService.dto.UserUpdateDto;
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.SystemUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SystemUserController {

	private final SystemUserService systemUserService;
	private final AzureBlobStorageService azureBlobStorageService;
	private final ModelMapper modelMapper;

	@PatchMapping("udpate")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> udpateUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
		final SystemUserInfoDto systemUserDto = new SystemUserInfoDto();
		systemUserDto.setUserName(userUpdateDto.getModifierUserDto().getUserName());
		systemUserDto.setOtp(userUpdateDto.getModifierUserDto().getOtp());
		systemUserService.authenticateSystemUserByOtp(systemUserDto.getUserName(), systemUserDto.getOtp());
		return ResponseEntity.ok(systemUserService.udpateUserDetails(userUpdateDto));
	}

	@DeleteMapping("remove")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody SystemUserInfoDto systemUser) {
		systemUserService.deleteSystemUserByUserName(systemUser);
		return ResponseEntity.ok(true);
	}

	@GetMapping("generate-otp")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public void generateOtp(@RequestParam String userName) {
		systemUserService.generateOtpForSystemUser(userName);
	}

	@PatchMapping("update-profile-pic")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> uploadSystemUserProfilePic(@RequestParam MultipartFile file,
			@RequestPart String userName, @RequestPart String otp) {
		final SystemUserInfo systemUser = systemUserService.authenticateSystemUserByOtp(userName, otp);
		boolean uploadAppUserProfile = azureBlobStorageService.uploadSystemUserProfile(file, systemUser);
		return ResponseEntity.ok(uploadAppUserProfile);
	}

	@GetMapping("get-by-username")
	public ResponseEntity<SystemUserResponseDto> getUserByUsername(@RequestParam String userName) {
		SystemUserInfo existingUser = systemUserService.getExistingSystemUser(userName);
		GenericMapper<SystemUserResponseDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserResponseDto.class, SystemUserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}
//
//	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
//	@GetMapping("list")
//	public ResponseEntity<List<SystemUserInfoDto>> getUsersList() {
//		return ResponseEntity.ok(systemUserService.getSystemUsersList());
//	}

}
