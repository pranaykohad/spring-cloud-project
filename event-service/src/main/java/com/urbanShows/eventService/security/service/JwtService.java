package com.urbanShows.eventService.security.service;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.urbanShows.eventService.security.authService.AuthService;
import com.urbanShows.eventService.security.dto.JwtTokenDto;
import com.urbanShows.eventService.security.exception.JwtParseException;
import com.urbanShows.eventService.security.util.Helper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {

	private UserDetailsServiceImpl userDetailsService;
	private AuthService authService;

//	public Boolean validateTokenForUserName(String token, String userName) {
//		final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//		boolean isTokenValidate = false;
//		try {
//			isTokenValidate = validateToken(token, userDetails);
//		} catch (Exception ex) {
//			throw new JwtParseException("Invalid Jwt Token");
//		}
//		return isTokenValidate;
//	}
//
//	public Boolean validateToken(String token, UserDetails userDetails) {
//		final String username = Helper.extractUsername(token);
//		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//	}
//
//	private Boolean isTokenExpired(String token) {
//		JwtTokenDto jwtToken = authService.findByToken(token);
//		return jwtToken == null || jwtToken.getExpiration().isBefore(LocalDateTime.now());
//	}

}
