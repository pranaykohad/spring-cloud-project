package com.urbanShows.userService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.AuthDto;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.SystemUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SystemUserAuthController {

	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final SystemUserService systemUserService;

	@GetMapping("generate-otp")
	public void generateOtp(@RequestParam String userName) {
		systemUserService.generateOtpForSystemUser(userName);
	}

	@PostMapping("login")
	public ResponseEntity<String> login(@Valid @RequestBody AuthDto authRequest) {
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return ResponseEntity.ok(jwtService.saveAndSendJwtTokenForSystemUser(authRequest.getUserName()));
		} else {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
	}
	
	@GetMapping("logout")
	public ResponseEntity<Boolean> logout(@RequestParam String token) {
		jwtService.invalidateToken(token);
		return ResponseEntity.ok(true);
	}

	@PostMapping("signup")
	public ResponseEntity<Boolean> signup(@Valid @RequestBody SystemUserInfoDto systemUser) {
		return ResponseEntity.ok(systemUserService.addSystemUser(systemUser));
	}

	

}
