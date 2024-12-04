package com.urbanShows.userService.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
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
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.internalAPIClient.EventServiceClient;
import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.SystemUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/user/system/auth")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SystemUserAuthController {

	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final EventServiceClient eventServiceClient;
	private final SystemUserService systemUserService;
	private final MessageProducer messageProducer;
	private final ModelMapper modelMapper;

	@GetMapping("event-name")
	public ResponseEntity<String> getEventName() {
		log.info("NAME TRACE: {}", "customer service called");
		messageProducer.sendStringMessage(KafkaTopicEnums.USER_LOGGED_IN.name(), "Hello, Pranay Kohad here, anybody home?");
		return ResponseEntity.ok(eventServiceClient.welcome().getBody());
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

	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody SystemUserInfoDto systemUser) {
		systemUserService.deleteSystemUserByUserName(systemUser);
		return ResponseEntity.ok(true);
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

	@GetMapping("get-by-name")
	public ResponseEntity<SystemUserInfoDto> getUserByName(@RequestParam String name) {
		SystemUserInfo existingUser = systemUserService.isSystemUserExists(name);
		GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	@GetMapping("list")
	public ResponseEntity<List<SystemUserInfoDto>> getUsersList() {
		return ResponseEntity.ok(systemUserService.getSystemUsersList());
	}

	// TODO: cannot change roles, cannot update other details with OTP
	@PatchMapping("udpate")
	public ResponseEntity<SystemUserInfoDto> udpateUser(@Valid @RequestBody SystemUserInfoDto userInfo) {
		return ResponseEntity.ok(systemUserService.udpate(userInfo));
	}

}
