package com.urbanShows.userService.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.urbanShows.userService.dto.AppUserLoginReqDto;
import com.urbanShows.userService.service.AppUserService;

import lombok.AllArgsConstructor;

@Aspect
@Component
@AllArgsConstructor
public class AppUserAuthenticationAspect {

	private final AppUserService appUserService;

	@Before("@annotation(AppUserAuthentication) && args(appUserLoginReqDto,..)")
	public void beforeControllerAction(AppUserLoginReqDto appUserLoginReqDto) {
		appUserService.authenticateAppUserByOtp(appUserLoginReqDto.getPhone(), appUserLoginReqDto.getOtp());
	}

}
