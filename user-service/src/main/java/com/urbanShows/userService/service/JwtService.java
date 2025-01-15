package com.urbanShows.userService.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.entity.JwtToken;
import com.urbanShows.userService.exception.JwtParseException;
import com.urbanShows.userService.repository.JwtTokenRepo;
import com.urbanShows.userService.util.JwtHelper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
	public String saveAndSendJwtTokenForSystemUser(String userName, Collection<? extends GrantedAuthority> collection) {
		return createAndSaveToken(userName, collection);
	}

	@Transactional
	public String generateTokenForAppUser(String phone, Collection<? extends GrantedAuthority> collection) {
		return createAndSaveToken(phone, collection);
	}

	public Boolean validateToken(String token, String useName) {
		final String username = JwtHelper.extractUsername(token);
		return (username.equals(useName) && !isTokenExpired(token));
	}

	private Boolean isTokenExpired(String token) {
		JwtToken jwtToken = jwtTokenRepo.findByToken(token);
		return jwtToken == null || jwtToken.getExpiration().isBefore(LocalDateTime.now());
	}

	private String createAndSaveToken(String id, Collection<? extends GrantedAuthority> collection) {
		final String authoritiesString = collection.stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		final String token = Jwts.builder().setSubject(id).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
				.claim("authorities", authoritiesString).signWith(JwtHelper.getSignKey(), SignatureAlgorithm.HS256)
				.compact();

		jwtTokenRepo.deleteByUsername(id);

		JwtToken jwtToken = new JwtToken();
		jwtToken.setToken(token);
		jwtToken.setUsername(id);
		jwtToken.setExpiration(EXPIRATION);
		jwtTokenRepo.save(jwtToken);

		return token;
	}
}
