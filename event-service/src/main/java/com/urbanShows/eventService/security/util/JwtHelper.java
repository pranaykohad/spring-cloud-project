package com.urbanShows.eventService.security.util;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.urbanShows.eventService.security.enums.Role;
import com.urbanShows.eventService.security.exception.JwtParseException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

public class JwtHelper {

	private JwtHelper() {
	}

	private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	public static List<GrantedAuthority> getLoggedinUserAuthorities(String token) {
		final Claims claims = Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		final String authoritiesString = claims.get("authorities", String.class);
		return Arrays.stream(authoritiesString.split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public static List<Role> getLoggedinUserRoles(String token) {
		final Claims claims = Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		final String authoritiesString = claims.get("authorities", String.class);
		return Arrays.stream(authoritiesString.split(",")).map(JwtHelper::fromAuthority).toList();
	}

	public static String extractJwt(HttpServletRequest httpServletRequest) {
		final String authHeader = httpServletRequest.getHeader("Authorization");
		String token = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		} else {
			throw new JwtParseException("Invalid Jwt");
		}
		return token;
	}

	public static Pair<String, String> extractUserNameAndToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String id = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			id = JwtHelper.extractUsername(token);
		}
		return Pair.of(id, token);
	}

	public static String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public static Boolean validateToken(String token, String useName) {
		final String username = JwtHelper.extractUsername(token);
		return (username.equals(useName) && !isTokenExpired(token));
	}

	public static Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public static Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public static UserDetails buildUser(String userName, String password, List<Role> roles) {
		final List<String> list = new ArrayList<>();
		roles.forEach(i -> list.add(i.name()));
		return User.builder().username(userName).password(password).roles(list.toArray(new String[0])).build();
	}

//	public static List<Role> getLoggedinUserRoles(Principal principal) {
//	if (principal instanceof Authentication authentication) {
//		return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
//				.map(Helper::fromAuthority).toList();
//	}
//	return List.of(); // No roles present
//}

	private static Boolean isTokenExpired(String token) {
		final Date expiration = extractExpiration(token);
		final LocalDateTime localDateTime = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return localDateTime.isBefore(LocalDateTime.now());
	}

	private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private static Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private static Role fromAuthority(String authority) {
		return Role.valueOf(authority.replace("ROLE_", ""));
	}

}
