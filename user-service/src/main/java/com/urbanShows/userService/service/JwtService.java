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

	private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	private static final LocalDateTime EXPIRATION = LocalDateTime.now().plusHours(24);

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public Boolean validateTokenForUserName(String token, String userName) {
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		boolean isTokenValidate = false;
		try {
			isTokenValidate = validateToken(token, userDetails);
		} catch (Exception ex) {
			throw new JwtParseException("Invalid Jwt Token");
		}
		return isTokenValidate;
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
		return createAndSaveToken(phone);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
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
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

		jwtTokenRepo.deleteByUsername(id);

		JwtToken jwtToken = new JwtToken();
		jwtToken.setToken(token); 
		jwtToken.setUsername(id);
		jwtToken.setExpiration(EXPIRATION);
		jwtTokenRepo.save(jwtToken);

		return token;
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
