package com.urbanShows.userService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.AppUserSigninReqDto;
import com.urbanShows.userService.service.AppUserService;
import com.urbanShows.userService.service.JwtService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/app/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AppUserAuthController {

	private final AppUserService appUserService;
	private final JwtService jwtService;

	// to save display name, phone, role and auth token in db
	@PostMapping("signin")
	public ResponseEntity<Boolean> register(@Valid @RequestBody AppUserSigninReqDto appUser) {
		return ResponseEntity.ok(appUserService.signinAppUser(appUser));
	}

	// login by phone and auth token
	@PostMapping("login")
	public ResponseEntity<String> login(@Valid @RequestBody AppUserInfoDto appUserDto) {
		appUserService.authenticateAppUserByOtp(appUserDto);
		return ResponseEntity.ok(jwtService.generateTokenForAppUser(appUserDto.getPhone()));
	}

	@GetMapping("logout")
	public ResponseEntity<Boolean> logout(@RequestParam String token) {
		jwtService.invalidateToken(token);
		return ResponseEntity.ok(true);
	}

	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody AppUserInfoDto appUserDto) {
		appUserService.authenticateAppUserByOtp(appUserDto);
		appUserService.deleteAppUser(appUserDto);
		return ResponseEntity.ok(true);
	}

	// TODO: cannot change roles,
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

}
