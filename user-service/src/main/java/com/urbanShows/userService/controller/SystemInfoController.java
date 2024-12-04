package com.urbanShows.userService.controller;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system-info")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SystemInfoController {

	private final Environment environment;

	@GetMapping("active-profile")
	public ResponseEntity<String[]> activeProfile() {
		return ResponseEntity.ok(environment.getActiveProfiles());
	}

	@GetMapping("version")
	public ResponseEntity<String> apVersion() {
		return ResponseEntity.ok(environment.getProperty("app.version"));
	}

}
