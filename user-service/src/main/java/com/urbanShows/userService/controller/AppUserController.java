package com.urbanShows.userService.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.service.AppUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AppUserController {

	private final AppUserService appUserService;

	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody AppUserInfoDto appUserDto) {
		appUserService.authenticateAppUserByOtp(appUserDto);
		appUserService.deleteAppUser(appUserDto);
		return ResponseEntity.ok(true);
	}

	// TODO: cannot change roles, for other update otp required
	@PatchMapping("udpate")
	public ResponseEntity<AppUserInfoDto> udpateUser(@Valid @RequestBody AppUserInfoDto appUser) {
		appUserService.authenticateAppUserByOtp(appUser);
		return ResponseEntity.ok(appUserService.udpate(appUser));
	}

	@GetMapping("test")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public ResponseEntity<String> testApppUser() {
		return ResponseEntity.ok("Test Ok");
	}
	
	@GetMapping("user-list")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<List<AppUserInfoDto>> getUserList() {
		return ResponseEntity.ok(appUserService.getUserList());
	}

}
