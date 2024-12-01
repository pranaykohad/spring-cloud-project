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

import com.urbanShows.userService.dto.AuthDto;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.internalAPIClient.EventServiceClient;
import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.SystemUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/system-user/auth")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SystemUserAuthController {

	private JwtService jwtService;

	private AuthenticationManager authenticationManager;
	
	private EventServiceClient eventServiceClient;
	
	private SystemUserService userService;
	
	private MessageProducer messageProducer;
	
	@GetMapping("event-name")
	public ResponseEntity<String> getEventName(){
		log.info("NAME TRACE: {}", "customer service called");
		messageProducer.sendMessage(KafkaTopicEnums.USER_LOGGED_IN.name(), "Hello, Pranay Kohad here, anybody home?");
		return ResponseEntity.ok(eventServiceClient.welcome().getBody());
	}
	
	@PostMapping("login")
	public ResponseEntity<String> login(@Valid @RequestBody AuthDto authRequest) {
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
	public ResponseEntity<Boolean> signup(@Valid @RequestBody SystemUserInfoDto userInfo) {
		return ResponseEntity.ok(userService.addSystemUser(userInfo));
	}

	@GetMapping("get-by-name")
	public ResponseEntity<SystemUserInfoDto> getUserByName(@RequestParam String name) {
		return ResponseEntity.ok(userService.getSystemUserByName(name));
	}

	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	@GetMapping("list")
	public ResponseEntity<List<SystemUserInfoDto>> getUsersList() {
		return ResponseEntity.ok(userService.getSystemUsersList());
	}

	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(String name) {
		userService.deleteUserByName(name);
		return ResponseEntity.ok(true);
	}

	@PatchMapping("udpate")
	public ResponseEntity<SystemUserInfoDto> udpateUser(@Valid @RequestBody SystemUserInfoDto userInfo) {
		return ResponseEntity.ok(userService.udpate(userInfo));
	}

}
