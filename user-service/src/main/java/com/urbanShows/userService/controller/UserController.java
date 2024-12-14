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
import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.dto.UserResponseDto;
import com.urbanShows.userService.dto.UserUpdateDto;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system")
@AllArgsConstructor
public class UserController {

	private final UserService systemUserService;
	private final AzureBlobStorageService azureBlobStorageService;
	private final ModelMapper modelMapper;
	private final JwtService jwtService;

	@GetMapping("logout")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> logout(@RequestParam String token) {
		jwtService.invalidateToken(token);
		return ResponseEntity.ok(true);
	}

	@PatchMapping("udpate")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> udpateUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
		final UserInfoDto systemUserDto = new UserInfoDto();
		systemUserDto.setUserName(userUpdateDto.getModifierUserDto().getUserName());
		systemUserDto.setOtp(userUpdateDto.getModifierUserDto().getOtp());
		systemUserService.authenticateSystemUserByOtp(systemUserDto.getUserName(), systemUserDto.getOtp());
		return ResponseEntity.ok(systemUserService.udpateUserDetails(userUpdateDto));
	}

	@DeleteMapping("remove")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody UserInfoDto systemUser) {
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
		final UserInfo systemUser = systemUserService.authenticateSystemUserByOtp(userName, otp);
		boolean uploadAppUserProfile = azureBlobStorageService.uploadSystemUserProfile(file, systemUser);
		return ResponseEntity.ok(uploadAppUserProfile);
	}

	@GetMapping("get-by-username")
	public ResponseEntity<UserResponseDto> getUserByUsername(@RequestParam String userName) {
		UserInfo existingUser = systemUserService.getExistingSystemUser(userName);
		GenericMapper<UserResponseDto, UserInfo> mapper = new GenericMapper<>(modelMapper,
				UserResponseDto.class, UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}
//
//	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
//	@GetMapping("list")
//	public ResponseEntity<List<SystemUserInfoDto>> getUsersList() {
//		return ResponseEntity.ok(systemUserService.getSystemUsersList());
//	}

}
