package com.urbanShows.userService.controller.systemUser;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.dto.SearchRequest;
import com.urbanShows.userService.dto.SystemUserBasicDto;
//import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.dto.SystemUserDto;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.dto.SystemUserInfoRespone;
import com.urbanShows.userService.dto.UserSecuredDetailsReq;
import com.urbanShows.userService.dto.UserSecuredDetailsRes;
import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.ProfileService;
import com.urbanShows.userService.service.SystemUserService;
import com.urbanShows.userService.util.JwtHelper;
import com.urbanShows.userService.util.RolesUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_USER', 'ROLE_SUPER_ADMIN_USER', 'ROLE_ORGANIZER_USER', 'ROLE_SYSTEM_USER')")
public class SystemUserController {

	private final SystemUserService systemUserService;
	private final ProfileService profileService;
	private final JwtService jwtService;
	private final ModelMapper modelMapper;

	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody SystemUserInfoDto systemUser) {
		systemUserService.deleteSystemUserByUserName(systemUser);
		return ResponseEntity.ok(true);
	}

	@GetMapping("generate-otp")
	public void generateOtpForSystemUser(@RequestParam String userName, @RequestParam String device) {
		// Generate OTP for active system user
		systemUserService.generateOtpForSystemUser(userName, device);
	}

	@GetMapping("loggedin-user-details")
	public ResponseEntity<SystemUserDto> getUserDetails(Principal principal) {
		final SystemUser existingUser = systemUserService.getActiveExistingSystemUser(principal.getName());
		final GenericMapper<SystemUserDto, SystemUser> mapper = new GenericMapper<>(modelMapper, SystemUserDto.class,
				SystemUser.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@GetMapping("basic-details")
	public ResponseEntity<SystemUserBasicDto> getUserBasicDetailsByUsername(@RequestParam String userName,
			Principal principal) {
		final SystemUser existingUser = systemUserService.getExistingSystemUser(userName);
		final GenericMapper<SystemUserBasicDto, SystemUser> mapper = new GenericMapper<>(modelMapper,
				SystemUserBasicDto.class, SystemUser.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@PatchMapping("basic-details")
	public ResponseEntity<Boolean> udpateBacisUserDetails(@RequestParam String targetUserName,
			@RequestParam(required = false) String displayName,
			@RequestParam(required = false) MultipartFile profilePicFile, Principal principal) {
		final SystemUser loggedInUser = systemUserService.getActiveExistingSystemUser(principal.getName());
		final SystemUser targetUser = systemUserService.getExistingSystemUser(targetUserName);
		RolesUtil.isHigherPriority(loggedInUser.getRoles().get(0), targetUser.getRoles().get(0));
		return ResponseEntity.ok(systemUserService.udpateBasicUserDetails(displayName, profilePicFile, targetUser));
	}

	@GetMapping("secured-details")
	public ResponseEntity<UserSecuredDetailsRes> getUserSecuredDetailsByUsername(@RequestParam String userName,
			Principal principal) {
		final SystemUser existingUser = systemUserService.getExistingSystemUser(userName);
		final GenericMapper<UserSecuredDetailsRes, SystemUser> mapper = new GenericMapper<>(modelMapper,
				UserSecuredDetailsRes.class, SystemUser.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@PatchMapping("secured-details")
	public ResponseEntity<Boolean> udpateSecuredUserDetails(@Valid @RequestBody UserSecuredDetailsReq securedDetails,
			Principal principal, HttpServletRequest request) {
		final SystemUser currentUser = systemUserService.validateActiveSystemUserByOtp(principal.getName(),
				securedDetails.getOtp());
		final SystemUser targetUser = systemUserService.getExistingSystemUser(securedDetails.getUserName());
		if (targetUser.getRoles().contains(Role.SUPER_ADMIN_USER)
				&& !currentUser.getRoles().contains(Role.SUPER_ADMIN_USER)) {
			throw new UnauthorizedException("You cannot perform this operation");
		}
		RolesUtil.isHigherPriority(currentUser.getRoles().get(0), targetUser.getRoles().get(0));
		final boolean isSelfUpdate = systemUserService.udpateSecuredUserDetails(securedDetails, targetUser,
				currentUser);
		if (isSelfUpdate) {
			jwtService.invalidateToken(JwtHelper.extractUserNameAndToken(request).getRight());
		}
		return ResponseEntity.ok(isSelfUpdate);
	}

	@PostMapping("user-list")
	public ResponseEntity<SystemUserInfoRespone> getUsersList(@RequestBody SearchRequest searchRequest,
			Principal principal) {
		systemUserService.getActiveExistingSystemUser(principal.getName());
		return ResponseEntity.ok(systemUserService.getSystemUsersList(searchRequest));
	}

	@GetMapping("activate-user")
	public ResponseEntity<Boolean> activateUser(@RequestParam String userName, @RequestParam String otp,
			Principal principal) {
		systemUserService.getActiveExistingSystemUser(principal.getName());
		return ResponseEntity.ok(systemUserService.activateUser(userName, otp));
	}

	@GetMapping("organiser-list")
	public ResponseEntity<List<String>> getOrganizerList(Principal principal) {
		systemUserService.getActiveExistingSystemUser(principal.getName());
		return ResponseEntity.ok(systemUserService.getOrganizerList());
	}

}
