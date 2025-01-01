package com.urbanShows.eventService.controller;

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

import com.urbanShows.eventService.dto.UserInfoDto;
import com.urbanShows.eventService.mapper.GenericMapper;
import com.urbanShows.eventService.security.dto.UserLoginDto;
import com.urbanShows.eventService.security.dto.UserResponseDto;
import com.urbanShows.eventService.security.dto.UserSigninDto;
import com.urbanShows.eventService.security.exception.AccessDeniedException;
import com.urbanShows.eventService.security.service.JwtService;
import com.urbanShows.eventService.security.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/event/auth")
@AllArgsConstructor
public class EventAuthController {

	private final UserService userService;
	private final JwtService jwtService;
	private final ModelMapper modelMapper;
	private final AuthenticationManager authenticationManager;


}
