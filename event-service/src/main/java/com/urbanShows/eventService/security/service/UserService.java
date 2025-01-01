package com.urbanShows.eventService.security.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.urbanShows.eventService.dto.UserInfoDto;
import com.urbanShows.eventService.enums.Status;
import com.urbanShows.eventService.security.authService.AuthService;
import com.urbanShows.eventService.security.dto.UserSigninDto;
import com.urbanShows.eventService.security.enums.Role;
import com.urbanShows.eventService.security.exception.UserInactiveException;
import com.urbanShows.eventService.security.exception.UserNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

	private final AuthService authService;
	private final PasswordEncoder passwordEncoder;

//	public UserInfoDto isUserActive(String userName) {
//		final UserInfoDto existingSystemUser = getExistingSystemUser(userName);
//		if (existingSystemUser.getStatus().equals(Status.ACTIVE)) {
//			return existingSystemUser;
//		} else {
//			throw new UserInactiveException("The user is not active. Please contact the supervisor for assistance");
//		}
//	}
//	
//	public UserInfoDto getExistingSystemUser(String userName) {
//		final UserInfoDto existingUser = authService.findByUserName(userName);
//		if (existingUser == null) {
//			throw new UserNotFoundException("User doesnot exists in the system");
//		}
//		return existingUser;
//	}

}
