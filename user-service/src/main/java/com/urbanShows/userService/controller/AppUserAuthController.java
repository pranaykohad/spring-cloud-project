package com.urbanShows.userService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.AppUserLoginReqDto;
import com.urbanShows.userService.dto.AppUserSigninReqDto;
import com.urbanShows.userService.service.AppUserService;
import com.urbanShows.userService.service.JwtService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/app/auth")
@AllArgsConstructor
public class AppUserAuthController {

	private final AppUserService appUserService;
	private final JwtService jwtService;

	@PostMapping("signin")
	public ResponseEntity<Boolean> register(@Valid @RequestBody AppUserSigninReqDto appUserSigninReqDto) {
		return ResponseEntity.ok(appUserService.signinAppUser(appUserSigninReqDto));
	}

	@PostMapping("login")
	public ResponseEntity<String> login(@Valid @RequestBody AppUserLoginReqDto appUserLoginReqDto) {
		appUserService.authenticateAppUserByOtp(appUserLoginReqDto.getPhone(), appUserLoginReqDto.getOtp());
		return ResponseEntity.ok(jwtService.generateTokenForAppUser(appUserLoginReqDto.getPhone()));
	}

	@GetMapping("logout")
	public ResponseEntity<Boolean> logout(@RequestParam String token) {
		jwtService.invalidateToken(token);
		return ResponseEntity.ok(true);
	}

}
