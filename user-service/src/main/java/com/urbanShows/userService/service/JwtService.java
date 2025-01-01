package com.urbanShows.userService.service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.entity.JwtToken;
import com.urbanShows.userService.exception.JwtParseException;
import com.urbanShows.userService.repository.JwtTokenRepo;
import com.urbanShows.userService.util.Helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtService {

	private JwtTokenRepo jwtTokenRepo;
	private UserDetailsServiceImpl userDetailsService;

	
	private static final LocalDateTime EXPIRATION = LocalDateTime.now().plusHours(24);

	public Boolean validateTokenForUserName(String token, String userName) {
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		boolean isTokenValidate = false;
		try {
			isTokenValidate = validateToken(token, userDetails.getUsername());
		} catch (Exception ex) {
			throw new JwtParseException("Invalid Jwt Token");
		}
		return isTokenValidate;
	}

	public void invalidateToken(String token) {
		jwtTokenRepo.deleteById(token);
	}

	@Transactional
	public String saveAndSendJwtTokenForSystemUser(String userName) {
		return createAndSaveToken(userName);
	}

	@Transactional
	public String generateTokenForAppUser(String phone) {
		return createAndSaveToken(phone);
	}
	
	public Boolean validateToken(String token, String useName) {
		final String username = Helper.extractUsername(token);
		return (username.equals(useName) && !isTokenExpired(token));
	}

	private Boolean isTokenExpired(String token) {
		JwtToken jwtToken = jwtTokenRepo.findByToken(token);
		return jwtToken == null || jwtToken.getExpiration().isBefore(LocalDateTime.now());
	}

	private String createAndSaveToken(String id) {
		Map<String, Object> claims = new HashMap<>();
		String token = Jwts.builder().setClaims(claims).setSubject(id)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
				.signWith(Helper.getSignKey(), SignatureAlgorithm.HS256).compact();

		jwtTokenRepo.deleteByUsername(id);

		JwtToken jwtToken = new JwtToken();
		jwtToken.setToken(token); 
		jwtToken.setUsername(id);
		jwtToken.setExpiration(EXPIRATION);
		jwtTokenRepo.save(jwtToken);

		return token;
	}
}
