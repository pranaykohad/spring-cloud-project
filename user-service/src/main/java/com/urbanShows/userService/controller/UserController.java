package com.urbanShows.userService.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.dto.LoggedinUserDetails;
import com.urbanShows.userService.dto.UserBasicDetails;
import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.dto.UserSecuredDetails;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system")
@AllArgsConstructor
public class UserController {

	private final UserService systemUserService;
	private final ModelMapper modelMapper;

	@PatchMapping("udpate-basic-details")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> udpateBacisUserDetails(@RequestParam(required = false) MultipartFile profilePicFile,
			@RequestParam(required = false) String displayName, Principal principal) {
		final UserInfo existingUserDetails = systemUserService.getExistingSystemUser(principal.getName());
		final UserBasicDetails userBasicDetails = new UserBasicDetails();
		userBasicDetails.setDisplayName(displayName);
		userBasicDetails.setProfilePicFile(profilePicFile);
		return ResponseEntity.ok(systemUserService.udpateBasicUserDetails(userBasicDetails, existingUserDetails));
	}

	@PatchMapping("udpate-secured-details")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> udpateSecuredUserDetails(@Valid @RequestBody UserSecuredDetails securedDetails) {
		final UserInfo existingUserDetails = systemUserService.authenticateSystemUserByOtp(securedDetails.getUserName(),
				securedDetails.getOtp());
		return ResponseEntity.ok(systemUserService.udpateSecuredUserDetails(securedDetails, existingUserDetails));
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

//	@PatchMapping("update-profile-pic")
//	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
//	public ResponseEntity<Boolean> uploadSystemUserProfilePic(@RequestParam MultipartFile file,
//			@RequestPart String userName, @RequestPart String otp) {
//		final UserInfo systemUser = systemUserService.authenticateSystemUserByOtp(userName, otp);
//		boolean uploadAppUserProfile = azureBlobStorageService.uploadSystemUserProfile(file, systemUser);
//		return ResponseEntity.ok(uploadAppUserProfile);
//	}

	@GetMapping("details")
	public ResponseEntity<LoggedinUserDetails> getUserDetails(Principal principal) {
		UserInfo existingUser = systemUserService.getExistingSystemUser(principal.getName());
		GenericMapper<LoggedinUserDetails, UserInfo> mapper = new GenericMapper<>(modelMapper,
				LoggedinUserDetails.class, UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@GetMapping("basic-details")
	public ResponseEntity<UserBasicDetails> getUserBasicDetailsByUsername(Principal principal) {
		UserInfo existingUser = systemUserService.getExistingSystemUser(principal.getName());
		GenericMapper<UserBasicDetails, UserInfo> mapper = new GenericMapper<>(modelMapper, UserBasicDetails.class,
				UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@GetMapping("secured-details")
	public ResponseEntity<UserSecuredDetails> getUserSecuredDetailsByUsername(Principal principal) {
		UserInfo existingUser = systemUserService.getExistingSystemUser(principal.getName());
		GenericMapper<UserSecuredDetails, UserInfo> mapper = new GenericMapper<>(modelMapper, UserSecuredDetails.class,
				UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

//
//	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
//	@GetMapping("list")
//	public ResponseEntity<List<SystemUserInfoDto>> getUsersList() {
//		return ResponseEntity.ok(systemUserService.getSystemUsersList());
//	}

}
