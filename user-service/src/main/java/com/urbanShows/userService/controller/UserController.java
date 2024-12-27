package com.urbanShows.userService.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.dto.LoggedinUserDetails;
import com.urbanShows.userService.dto.UserBasicDetails;
import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.dto.UserInfoListDto;
import com.urbanShows.userService.dto.UserSecuredDetailsReq;
import com.urbanShows.userService.dto.UserSecuredDetailsRes;
import com.urbanShows.userService.dto.UserActivationDto;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.UserService;
import com.urbanShows.userService.util.RolesUtil;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_USER', 'ROLE_SUPER_ADMIN_USER')")
public class UserController {

	private final UserService systemUserService;
	private final ModelMapper modelMapper;

	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody UserInfoDto systemUser) {
		systemUserService.deleteSystemUserByUserName(systemUser);
		return ResponseEntity.ok(true);
	}

	@GetMapping("generate-otp")
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
		final UserInfo existingUser = systemUserService.isUserActive(principal.getName());
		final GenericMapper<LoggedinUserDetails, UserInfo> mapper = new GenericMapper<>(modelMapper,
				LoggedinUserDetails.class, UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@GetMapping("basic-details")
	public ResponseEntity<UserBasicDetails> getUserBasicDetailsByUsername(@RequestParam String userName,
			Principal principal) {
		systemUserService.isUserActive(principal.getName());
		final UserInfo existingUser = systemUserService.getExistingSystemUser(userName);
		final GenericMapper<UserBasicDetails, UserInfo> mapper = new GenericMapper<>(modelMapper,
				UserBasicDetails.class, UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@PatchMapping("update-basic-details")
	public ResponseEntity<Boolean> udpateBacisUserDetails(@RequestParam String userName,
			@RequestParam(required = false) MultipartFile profilePicFile,
			@RequestParam(required = false) String displayName, Principal principal) {
		final UserInfo currentUser = systemUserService.isUserActive(principal.getName());
		final UserInfo targetUser = systemUserService.getExistingSystemUser(userName);
		RolesUtil.isHigherPriority(currentUser.getRoles().get(0), targetUser.getRoles().get(0));
		final UserBasicDetails userBasicDetails = new UserBasicDetails();
		userBasicDetails.setDisplayName(displayName);
		userBasicDetails.setProfilePicFile(profilePicFile);
		return ResponseEntity.ok(systemUserService.udpateBasicUserDetails(userBasicDetails, targetUser));
	}

	@GetMapping("secured-details")
	public ResponseEntity<UserSecuredDetailsRes> getUserSecuredDetailsByUsername(@RequestParam String userName,
			Principal principal) {
		systemUserService.isUserActive(principal.getName());
		final UserInfo existingUser = systemUserService.getExistingSystemUser(userName);
		final GenericMapper<UserSecuredDetailsRes, UserInfo> mapper = new GenericMapper<>(modelMapper,
				UserSecuredDetailsRes.class, UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@PatchMapping("update-secured-details")
	public ResponseEntity<Boolean> udpateSecuredUserDetails(@Valid @RequestBody UserSecuredDetailsReq securedDetails,
			Principal principal) {
		systemUserService.isUserActive(principal.getName());
		final UserInfo currentUser = systemUserService.authenticateSystemUserByOtp(principal.getName(),
				securedDetails.getOtp());
		final UserInfo targetUser = systemUserService.getExistingSystemUser(securedDetails.getUserName());
		if (targetUser.getRoles().contains(Role.SUPER_ADMIN_USER)
				&& securedDetails.getStatus().equals(com.urbanShows.userService.enums.Status.INACTIVE)) {
			throw new UnauthorizedException("Super Admin users cannot be deactivated");
		}
		RolesUtil.isHigherPriority(currentUser.getRoles().get(0), targetUser.getRoles().get(0));
		return ResponseEntity
				.ok(systemUserService.udpateSecuredUserDetails(securedDetails, targetUser, principal.getName()));
	}

	@GetMapping("user-list")
	public ResponseEntity<UserInfoListDto> getUsersList(Principal principal) {
		systemUserService.isUserActive(principal.getName());
		return ResponseEntity.ok(systemUserService.getSystemUsersList());
	}

	@PostMapping("user-activation")
	public ResponseEntity<Boolean> userValidation(@RequestBody UserActivationDto userActivationDto, Principal principal) {
		systemUserService.isUserActive(principal.getName());
		return ResponseEntity.ok(systemUserService.suerActivation(userActivationDto));
	}

}
