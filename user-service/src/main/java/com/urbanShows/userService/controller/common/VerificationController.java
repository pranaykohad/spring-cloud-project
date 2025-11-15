package com.urbanShows.userService.controller.common;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.SystemUserService;
import com.urbanShows.userService.util.JwtHelper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/verification")
@AllArgsConstructor
public class VerificationController {

	private final JwtService jwtService;
	private final SystemUserService systemUserService;

	@GetMapping("validate-token")
	public ResponseEntity<Boolean> findByToken(Principal principal, HttpServletRequest request) {
		if (principal != null) {
			return ResponseEntity.ok(jwtService.validateTokenForUserName(
					JwtHelper.extractUserNameAndToken(request).getRight(), principal.getName()));
		}
		return ResponseEntity.ok(false);
	}

	@GetMapping("validate-organizer")
	public ResponseEntity<Boolean> validateOrganizer(@RequestParam String userName, Principal principal) {
		systemUserService.getActiveExistingSystemUser(principal.getName());
		return ResponseEntity.ok(systemUserService.isValidOrganizer(userName));
	}

	@GetMapping("authorize-user")
	public ResponseEntity<Boolean> usernameAndOtpvalidation(@RequestParam String otp, Principal principal) {
		final SystemUser currentUser = systemUserService.validateActiveSystemUserByOtp(principal.getName(), otp);
		return ResponseEntity.ok(currentUser != null);
	}
	
	@GetMapping("logout")
	public ResponseEntity<Boolean> logout(HttpServletRequest request) {
		jwtService.invalidateToken(JwtHelper.extractUserNameAndToken(request).getRight());
		return ResponseEntity.ok(true);
	}

}
