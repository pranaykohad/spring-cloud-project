package com.urbanShows.userService.controller.appUser;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.AppUserLoginDto;
import com.urbanShows.userService.dto.AppUserRegisterDto;
import com.urbanShows.userService.dto.UserInternalInfo;
import com.urbanShows.userService.dto.UserResponseDto;
import com.urbanShows.userService.entity.AppUser;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.AppUserService;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.util.JwtHelper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user-auth/app")
@AllArgsConstructor
public class AppUserAuthController {

	private final AppUserService appUserService;
	private final JwtService jwtService;
	private final ModelMapper modelMapper;

	@PostMapping("register")
	public ResponseEntity<Boolean> register(@Valid @RequestBody AppUserRegisterDto appUserSigninReqDto) {
		return ResponseEntity.ok(appUserService.registerAppUser(appUserSigninReqDto));
	}

	@PostMapping("login")
	public ResponseEntity<UserResponseDto> login(@Valid @RequestBody AppUserLoginDto appUserLoginReqDto,
			Principal principal) {

		// Authenticate user by phone and OTP
		appUserService.authenticateAppUserByOtp(appUserLoginReqDto.getPhone(), appUserLoginReqDto.getOtp());

		// Check is user exists and extract List<authority>
		final AppUser existingAppUser = appUserService.getExistingAppUser(appUserLoginReqDto.getPhone());

		// Create, save and add JWT token in response
		final String tokenForAppUser = jwtService.generateTokenForAppUser(appUserLoginReqDto.getPhone(),
				JwtHelper.rolesToAuthorities(existingAppUser.getRoles()));
		final GenericMapper<UserResponseDto, AppUser> mapper = new GenericMapper<>(modelMapper, UserResponseDto.class,
				AppUser.class);
		final UserResponseDto apppUserResponseDto = mapper.entityToDto(existingAppUser);
		apppUserResponseDto.setJwt(tokenForAppUser);
		return ResponseEntity.ok(apppUserResponseDto);
	}

	@GetMapping("loggedin-user-info")
	public ResponseEntity<UserInternalInfo> getLoggedinAppUserInfo(Principal principal) {
		if (principal != null) {
			final GenericMapper<UserInternalInfo, AppUser> mapper = new GenericMapper<>(modelMapper,
					UserInternalInfo.class, AppUser.class);
			final AppUser userActive = appUserService.getExistingAppUser(principal.getName());
			return ResponseEntity.ok(mapper.entityToDto(userActive));
		}
		throw new UnauthorizedException("Unauthorized access");
	}

}
