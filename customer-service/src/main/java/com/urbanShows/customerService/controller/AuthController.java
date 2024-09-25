package com.urbanShows.customerService.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.customerService.dto.AuthDto;
import com.urbanShows.customerService.dto.UserInfoDto;
import com.urbanShows.customerService.exceptionHandler.UserNotFoundException;
import com.urbanShows.customerService.internalAPIClient.EventServiceClient;
import com.urbanShows.customerService.service.JwtService;
import com.urbanShows.customerService.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/customer/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

	private JwtService jwtService;

	private AuthenticationManager authenticationManager;
	
	private EventServiceClient eventServiceClient;
	
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//	@PreAuthorize("hasAuthority('ROLE_USER')")

	@PostMapping("login")
	public ResponseEntity<String> login(@RequestBody AuthDto authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
//			ResponseEntity<String> welcome = eventServiceClient.welcome();
//			return ResponseEntity.ok(welcome.getBody());
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
	
	private UserService userService;

	@PostMapping("signup")
	public ResponseEntity<Boolean> signup(@Valid @RequestBody UserInfoDto userInfo) {
		return ResponseEntity.ok(userService.addUser(userInfo));
	}

	@GetMapping("get-by-name")
	public ResponseEntity<UserInfoDto> getUserByName(@RequestParam String name) {
		return ResponseEntity.ok(userService.getUserByName(name));
	}

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
