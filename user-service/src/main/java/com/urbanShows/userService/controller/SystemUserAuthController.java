package com.urbanShows.userService.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.SystemUserLoginDto;
import com.urbanShows.userService.dto.UserResponseDto;
import com.urbanShows.userService.dto.SystemUserSigninDto;
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.exceptionHandler.AccessDeniedException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.SystemUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system/auth")
@AllArgsConstructor
public class SystemUserAuthController {

	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final SystemUserService systemUserService;
	private final ModelMapper modelMapper;

	@PostMapping("signup")
	public ResponseEntity<Boolean> signup(@Valid @RequestBody SystemUserSigninDto systemUserSigninDto) {
		return ResponseEntity.ok(systemUserService.addSystemUser(systemUserSigninDto));
	}

	@PostMapping("login")
	public ResponseEntity<UserResponseDto> login(@Valid @RequestBody SystemUserLoginDto systemUserLoginDto) {
		try {
			final Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(systemUserLoginDto.getUserName(),
							systemUserLoginDto.getPassword()));
			if (authentication.isAuthenticated()) {
				final String jwtToken = jwtService.saveAndSendJwtTokenForSystemUser(systemUserLoginDto.getUserName());
				final SystemUserInfo existingSystemUser = systemUserService
						.getExistingSystemUser(systemUserLoginDto.getUserName());
				final GenericMapper<UserResponseDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
						UserResponseDto.class, SystemUserInfo.class);
				final UserResponseDto systemUserResponseDto = mapper.entityToDto(existingSystemUser);
				systemUserResponseDto.setJwt(jwtToken);
				return ResponseEntity.ok(systemUserResponseDto);
			} else {
				throw new AccessDeniedException("User name or password is not correct");
			}
		} catch (Exception e) {
			throw new AccessDeniedException("User name or password is not correct");
		}
	}

	@GetMapping("validate-jwt")
	public ResponseEntity<Boolean> validateJwt(@RequestParam String jwtToken, @RequestParam String userName) {
		SystemUserInfo existingUser = systemUserService.getExistingSystemUser(userName);
		return ResponseEntity.ok(jwtService.validateTokenForUserName(jwtToken, existingUser.getUserName()));
	}
	
}
