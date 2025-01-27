package com.urbanShows.userService.controller;

import java.security.Principal;
import java.util.List;

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

import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.dto.LoggedinUserDetails;
import com.urbanShows.userService.dto.SearchRequest;
import com.urbanShows.userService.dto.UserBasicDetails;
import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.dto.UserInfoRespone;
import com.urbanShows.userService.dto.UserSecuredDetailsReq;
import com.urbanShows.userService.dto.UserSecuredDetailsRes;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.UserService;
import com.urbanShows.userService.util.JwtHelper;
import com.urbanShows.userService.util.RolesUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_USER', 'ROLE_SUPER_ADMIN_USER', 'ROLE_ORGANIZER_USER', 'ROLE_ADMIN_USER')")
public class UserController {

	private final UserService systemUserService;
	private final JwtService jwtService;
	private final ModelMapper modelMapper;

	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody UserInfoDto systemUser) {
		systemUserService.deleteSystemUserByUserName(systemUser);
		return ResponseEntity.ok(true);
	}

	@GetMapping("generate-otp")
	public void generateOtpForSystemUser(@RequestParam String userName, @RequestParam String device) {
		// Generate OTP for active system user
		systemUserService.generateOtpForSystemUser(userName, device);
	}

	@GetMapping("loggedin-user-details")
	public ResponseEntity<LoggedinUserDetails> getUserDetails(Principal principal) {
		final UserInfo existingUser = systemUserService.getActiveExistingSystemUser(principal.getName());
		final GenericMapper<LoggedinUserDetails, UserInfo> mapper = new GenericMapper<>(modelMapper,
				LoggedinUserDetails.class, UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@GetMapping("basic-details")
	public ResponseEntity<UserBasicDetails> getUserBasicDetailsByUsername(@RequestParam String userName,
			Principal principal) {
		final UserInfo existingUser = systemUserService.getExistingSystemUser(userName);
		final GenericMapper<UserBasicDetails, UserInfo> mapper = new GenericMapper<>(modelMapper,
				UserBasicDetails.class, UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@PatchMapping("update-basic-details")
	public ResponseEntity<Boolean> udpateBacisUserDetails(@RequestParam String userName,
			@RequestParam(required = false) MultipartFile profilePicFile,
			@RequestParam(required = false) String displayName, Principal principal) {
		final UserInfo currentUser = systemUserService.getActiveExistingSystemUser(principal.getName());
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
		final UserInfo existingUser = systemUserService.getExistingSystemUser(userName);
		final GenericMapper<UserSecuredDetailsRes, UserInfo> mapper = new GenericMapper<>(modelMapper,
				UserSecuredDetailsRes.class, UserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@PatchMapping("update-secured-details")
	public ResponseEntity<Boolean> udpateSecuredUserDetails(@Valid @RequestBody UserSecuredDetailsReq securedDetails,
			Principal principal, HttpServletRequest request) {
		final UserInfo currentUser = systemUserService.validateActiveSystemUserByOtp(principal.getName(),
				securedDetails.getOtp());
		final UserInfo targetUser = systemUserService.getExistingSystemUser(securedDetails.getUserName());
		if (targetUser.getRoles().contains(Role.SUPER_ADMIN_USER)
				&& !currentUser.getRoles().contains(Role.SUPER_ADMIN_USER)) {
			throw new UnauthorizedException("You cannot perform this operation");
		} 
		RolesUtil.isHigherPriority(currentUser.getRoles().get(0), targetUser.getRoles().get(0));
		final boolean udpateSecuredUserDetails = systemUserService.udpateSecuredUserDetails(securedDetails, targetUser, currentUser);
		if(udpateSecuredUserDetails) {
			jwtService.invalidateToken(JwtHelper.extractUserNameAndToken(request).getRight());
		}
		return ResponseEntity.ok(udpateSecuredUserDetails);
	}

	@PostMapping("user-list")
	public ResponseEntity<UserInfoRespone> getUsersList(@RequestBody SearchRequest searchRequest, Principal principal) {
		systemUserService.getActiveExistingSystemUser(principal.getName());
		return ResponseEntity.ok(systemUserService.getSystemUsersList(searchRequest));
	}

	@GetMapping("activate-user")
	public ResponseEntity<Boolean> activateUser(@RequestParam String userName, @RequestParam String otp,
			Principal principal) {
		systemUserService.getExistingSystemUser(principal.getName());
		return ResponseEntity.ok(systemUserService.activateUser(userName, otp));
	}

	@GetMapping("organiser-list")
	public ResponseEntity<List<String>> getOrganizerList(Principal principal) {
		systemUserService.getActiveExistingSystemUser(principal.getName());
		return ResponseEntity.ok(systemUserService.getOrganizerList());
	}

}
