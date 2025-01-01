package com.urbanShows.userService.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.UserLoginDto;
import com.urbanShows.userService.dto.UserResponseDto;
import com.urbanShows.userService.dto.UserSigninDto;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.exception.AccessDeniedException;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.UserService;
import com.urbanShows.userService.util.Helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system/auth")
@AllArgsConstructor
public class UserAuthController {

	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final UserService systemUserService;
	private final ModelMapper modelMapper;

	@PostMapping("signup")
	public ResponseEntity<Boolean> signup(@Valid @RequestBody UserSigninDto systemUserSigninDto) {
		return ResponseEntity.ok(systemUserService.addSystemUser(systemUserSigninDto));
	}

	@PostMapping("login")
	public ResponseEntity<UserResponseDto> login(@Valid @RequestBody UserLoginDto systemUserLoginDto) {
		final UserInfo existingSystemUser = systemUserService.isUserActive(systemUserLoginDto.getUserName());
		try {
			final Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(systemUserLoginDto.getUserName(),
							systemUserLoginDto.getPassword()));
			if (authentication.isAuthenticated()) {
				final String jwtToken = jwtService.saveAndSendJwtTokenForSystemUser(systemUserLoginDto.getUserName());
				final GenericMapper<UserResponseDto, UserInfo> mapper = new GenericMapper<>(modelMapper,
						UserResponseDto.class, UserInfo.class);
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

	@GetMapping("logout")
	public ResponseEntity<Boolean> logout(@RequestParam String token) {
		jwtService.invalidateToken(token);
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("loggedin-user-info")
	public ResponseEntity<UserInfo> getLoggedinUserInfo(Principal principal) {
		if(principal != null) {
			return ResponseEntity.ok(systemUserService.isUserActive(principal.getName()));
		}
		throw new UnauthorizedException("Unauthorized access");
	}
	
	@GetMapping("validate-token")
	public ResponseEntity<Boolean> findByToken(Principal principal, HttpServletRequest request) {
		if(principal != null) {
			return ResponseEntity.ok(jwtService.validateTokenForUserName(Helper.extractUserNameAndToken(request).getRight()
					, principal.getName()));
		}
		throw new UnauthorizedException("Unauthorized access");
	}
	
}
