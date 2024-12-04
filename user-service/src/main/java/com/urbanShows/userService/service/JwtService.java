package com.urbanShows.userService.service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.entity.JwtToken;
import com.urbanShows.userService.repository.JwtTokenRepo;

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

	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
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
		return createAndSaveTokenForAppUser(phone);
	} 
	
	private String createAndSaveTokenForAppUser(String phone) {
		Map<String, Object> claims = new HashMap<>();
		String token = Jwts.builder().setClaims(claims).setSubject(phone)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 )) // Set expiration time - 1 min
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

		jwtTokenRepo.deleteByUsername(phone);

		JwtToken jwtToken = new JwtToken();
		jwtToken.setToken(token);
		jwtToken.setUsername(phone);
		jwtToken.setExpiration(LocalDateTime.now().plusMinutes(1)); // Set expiration time - 1 min 
		jwtTokenRepo.save(jwtToken);

		return token;
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		JwtToken jwtToken = jwtTokenRepo.findByToken(token);
		return jwtToken == null || jwtToken.getExpiration().isBefore(LocalDateTime.now());
	}

	private String createAndSaveToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		String token = Jwts.builder().setClaims(claims).setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Set expiration time - 30 min
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

		jwtTokenRepo.deleteByUsername(userName);

		JwtToken jwtToken = new JwtToken();
		jwtToken.setToken(token);
		jwtToken.setUsername(userName);
		jwtToken.setExpiration(LocalDateTime.now().plusMinutes(30)); // Set expiration time - 30 min
		jwtTokenRepo.save(jwtToken);

		return token;
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
