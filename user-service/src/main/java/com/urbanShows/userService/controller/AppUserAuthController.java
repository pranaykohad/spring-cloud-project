package com.urbanShows.userService.controller;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.config.MessageProducer;
import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.internalAPIClient.EventServiceClient;
import com.urbanShows.userService.service.AppUserService;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.SystemUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/app-user/auth")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AppUserAuthController {

	private AppUserService appUserService;

	private JwtService jwtService;

	// to save display name, phone, role and otp in db
	@PostMapping("signup")
	public ResponseEntity<Boolean> signup(@Valid @RequestBody AppUserInfoDto userInfo) {
		return ResponseEntity.ok(appUserService.addAppUser(userInfo));
	}
	
	// generate otp from phone
	@PostMapping("generate-otp")
	public ResponseEntity<String> generateOtp(@Valid String phone) {
		AppUserInfoDto existingAppUser = appUserService.getAppUserByPhone(phone);
		if (existingAppUser != null) {
			return ResponseEntity.ok(appUserService.generateAndSaveOtp(existingAppUser));
		} else {
			throw new UserNotFoundException("invalid user request !");
		}
	}

	// generate token from phone and otp
	@PostMapping("login")
	public ResponseEntity<String> login(@Valid @RequestBody AppUserInfoDto userInfo) {
		if (appUserService.verifyOtp(userInfo)) {
			return ResponseEntity.ok(jwtService.generateTokenForAppUser(userInfo.getPhone()));
		} else {
			throw new UserNotFoundException("invalid user request !");
		}
	}
	
	@GetMapping("logout")
	public ResponseEntity<Boolean> logout(@RequestParam String token) {
		jwtService.invalidateToken(token);
		return ResponseEntity.ok(true);
	}
	
	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody AppUserInfoDto userInfo) {
		if(appUserService.verifyOtp(userInfo)) {
			appUserService.deleteUserByPhone(userInfo.getPhone());
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.ok(false);
	}

	@PatchMapping("udpate")
	public ResponseEntity<AppUserInfoDto> udpateUser(@Valid @RequestBody AppUserInfoDto userInfo) {
		return ResponseEntity.ok(appUserService.udpate(userInfo));
	}


	@GetMapping("test")
	@PreAuthorize("hasAuthority('ROLE_APP_USER')")
	public ResponseEntity<String> testApppUser() {
		return ResponseEntity.ok("Test Ok");
	}

}
