package com.urbanShows.userService.controller;


import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import com.urbanShows.userService.dto.AuthDto;
import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.internalAPIClient.EventServiceClient;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/user/auth")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthController {

	private JwtService jwtService;

	private AuthenticationManager authenticationManager;
	
	private EventServiceClient eventServiceClient;
	
	private UserService userService;
	
	private MessageProducer messageProducer;
	
	private final Environment environment;
	
	@GetMapping("active-profile")
	public ResponseEntity<String[]> activeProfile() {
		String[] activeProfiles = environment.getActiveProfiles();
		return ResponseEntity.ok(activeProfiles);
	}
	
	@GetMapping("event-name")
	public ResponseEntity<String> getEventName(){
		log.info("NAME TRACE: {}", "customer service called");
		messageProducer.sendMessage("Hello, Pranay Kohad here, anybody home?");
		return ResponseEntity.ok(eventServiceClient.welcome().getBody());
	}
	
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//	@PreAuthorize("hasAuthority('ROLE_USER')")

	@PostMapping("login")
	public ResponseEntity<String> login(@RequestBody AuthDto authRequest) {
		log.info("login API called..........");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return ResponseEntity.ok(jwtService.generateToken(authRequest.getUsername()));
		} else {
			throw new UserNotFoundException("invalid user request !");
		}
	}
	

	@GetMapping("logout")
	public ResponseEntity<Boolean> logout(@RequestParam String token) {
		jwtService.invalidateToken(token);
		return ResponseEntity.ok(true);
	}
	

	@PostMapping("signup")
	public ResponseEntity<Boolean> signup(@Valid @RequestBody UserInfoDto userInfo) {
		return ResponseEntity.ok(userService.addUser(userInfo));
	}

	@GetMapping("get-by-name")
	public ResponseEntity<UserInfoDto> getUserByName(@RequestParam String name) {
		UserInfoDto userByName = userService.getUserByName(name);
		log.info("NAME: {}", userByName);
		return ResponseEntity.ok(userByName);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("list")
	public ResponseEntity<List<UserInfoDto>> getUsersList() {
		return ResponseEntity.ok(userService.getUsersList());
	}

	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(String name) {
		userService.deleteUserByName(name);
		return ResponseEntity.ok(true);
	}

	@PatchMapping("udpate")
	public ResponseEntity<UserInfoDto> udpateUser(@RequestBody UserInfoDto userInfo) {
		return ResponseEntity.ok(userService.udpate(userInfo));
	}

}
