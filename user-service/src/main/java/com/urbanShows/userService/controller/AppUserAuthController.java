package com.urbanShows.userService.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.AppUserLoginReqDto;
import com.urbanShows.userService.dto.AppUserSigninReqDto;
import com.urbanShows.userService.dto.UserInternalInfo;
import com.urbanShows.userService.dto.UserResponseDto;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.AppUserService;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.util.Helper;
import com.urbanShows.userService.util.JwtHelper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/app/auth")
@AllArgsConstructor
public class AppUserAuthController {

	private final AppUserService appUserService;
	private final JwtService jwtService;
	private final ModelMapper modelMapper;

	@PostMapping("signup")
	public ResponseEntity<Boolean> register(@Valid @RequestBody AppUserSigninReqDto appUserSigninReqDto) {
		return ResponseEntity.ok(appUserService.signinAppUser(appUserSigninReqDto));
	}

	@PostMapping("login")
	public ResponseEntity<UserResponseDto> login(@Valid @RequestBody AppUserLoginReqDto appUserLoginReqDto,
			Principal principal) {

		// Authenticate user by phone and OTP
		appUserService.authenticateAppUserByOtp(appUserLoginReqDto.getPhone(), appUserLoginReqDto.getOtp());

		// Check is user exists and extract List<authority>
		final AppUserInfo existingAppUser = appUserService.getExistingAppUser(appUserLoginReqDto.getPhone());

		// Create, save and add JWT token in response
		final String tokenForAppUser = jwtService.generateTokenForAppUser(appUserLoginReqDto.getPhone(),
				JwtHelper.rolesToAuthorities(existingAppUser.getRoles()));
		final GenericMapper<UserResponseDto, AppUserInfo> mapper = new GenericMapper<>(modelMapper,
				UserResponseDto.class, AppUserInfo.class);
		final UserResponseDto apppUserResponseDto = mapper.entityToDto(existingAppUser);
		apppUserResponseDto.setJwt(tokenForAppUser);
		return ResponseEntity.ok(apppUserResponseDto);
	}

	@GetMapping("loggedin-app-user-info")
	public ResponseEntity<UserInternalInfo> getLoggedinAppUserInfo(Principal principal) {
		if (principal != null) {
			final GenericMapper<UserInternalInfo, AppUserInfo> mapper = new GenericMapper<>(modelMapper,
					UserInternalInfo.class, AppUserInfo.class);
			final AppUserInfo userActive = appUserService.getExistingAppUser(principal.getName());
			return ResponseEntity.ok(mapper.entityToDto(userActive));
		}
		throw new UnauthorizedException("Unauthorized access");
	}

}
