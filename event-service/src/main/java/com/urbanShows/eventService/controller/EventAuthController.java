package com.urbanShows.eventService.controller;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.eventService.security.service.JwtService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/event/auth")
@AllArgsConstructor
public class EventAuthController {

	private final JwtService jwtService;
	private final ModelMapper modelMapper;
	private final AuthenticationManager authenticationManager;

}
