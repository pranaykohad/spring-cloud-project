package com.urbanShows.userService.controller;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/app-info")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserAppInfo {

	private final Environment environment;

	@GetMapping("active-profile")
	public ResponseEntity<String[]> activeProfile() {
		return ResponseEntity.ok(environment.getActiveProfiles());
	}

	@GetMapping("version")
	public ResponseEntity<String> apVersion() {
		return ResponseEntity.ok(environment.getProperty("app.version"));
	}

	@GetMapping("csrf-token")
	public String getCsrfToken(CsrfToken csrfToken) {
		return csrfToken.getToken();
	}

}
