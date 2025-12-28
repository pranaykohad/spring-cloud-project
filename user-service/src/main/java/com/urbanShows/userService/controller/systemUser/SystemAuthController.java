package com.urbanShows.userService.controller.systemUser;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.UserInternalInfo;
import com.urbanShows.userService.dto.SystemUserLoginDto;
import com.urbanShows.userService.dto.UserResponseDto;
import com.urbanShows.userService.dto.SystemUserRegisterDto;
import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.exception.AccessDeniedException;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.service.SystemUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/auth/system")
@AllArgsConstructor
public class SystemAuthController {

	private final SystemUserService systemUserService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final ModelMapper modelMapper;

	@PostMapping("register")
	public ResponseEntity<Boolean> register(@Valid @RequestBody SystemUserRegisterDto systemUserSigninDto) {
		return ResponseEntity.ok(systemUserService.registerSystemUser(systemUserSigninDto));
	}

	@PostMapping("login")
	public ResponseEntity<UserResponseDto> login(@Valid @RequestBody SystemUserLoginDto systemUserLoginDto) {
		final SystemUser existingSystemUser = systemUserService.getExistingSystemUser(systemUserLoginDto.getUserName());
		try {
			// Extract spring authentication object
			final Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(systemUserLoginDto.getUserName(),
							systemUserLoginDto.getPassword()));
			if (authentication.isAuthenticated()) {
				
				// Create, save and add JWT token in response
				final String jwtToken = jwtService.saveAndSendJwtTokenForSystemUser(systemUserLoginDto.getUserName(), authentication.getAuthorities());
				final GenericMapper<UserResponseDto, SystemUser> mapper = new GenericMapper<>(modelMapper,
						UserResponseDto.class, SystemUser.class);
				final UserResponseDto systemUserResponseDto = mapper.entityToDto(existingSystemUser);
				systemUserResponseDto.setJwt(jwtToken);
				return ResponseEntity.ok(systemUserResponseDto);
			} else {
				throw new AccessDeniedException("Bad credentials or account is not active");
			}
		} catch (Exception e) {
			throw new AccessDeniedException("Bad credentials or account is not active");
		}
	}

	@GetMapping("loggedin-user-info")
	public ResponseEntity<UserInternalInfo> getLoggedinSystemUserInfo(Principal principal) {
		if (principal != null) {
			final GenericMapper<UserInternalInfo, SystemUser> mapper = new GenericMapper<>(modelMapper,
					UserInternalInfo.class, SystemUser.class);
			final SystemUser userActive = systemUserService.getActiveExistingSystemUser(principal.getName());
			return ResponseEntity.ok(mapper.entityToDto(userActive));
		}
		throw new UnauthorizedException("Unauthorized access");
	}

}
