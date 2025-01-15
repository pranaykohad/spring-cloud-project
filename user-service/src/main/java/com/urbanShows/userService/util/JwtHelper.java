package com.urbanShows.userService.util;

import java.security.Key;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.urbanShows.userService.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

public class JwtHelper {

	private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	private JwtHelper() {
	}

	public static Collection<? extends GrantedAuthority> rolesToAuthorities(List<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).toList();
	}

	public static List<Role> authoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().map(GrantedAuthority::getAuthority)
				.filter(authority -> authority.startsWith("ROLE_"))
				.map(authority -> Role.valueOf(authority.substring(5))).toList();
	}

	public static List<Role> getLoggedinUserRoles(Principal principal) {
		if (principal instanceof Authentication authentication) {
			return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.map(JwtHelper::fromAuthority).toList();
		}
		return List.of();
	}

	public static UserDetails buildUser(String userName, String password, List<Role> roles) {
		final List<String> list = new ArrayList<>();
		roles.forEach(i -> list.add(i.name()));
		return User.builder().username(userName).password(password).roles(list.toArray(new String[0])).build();
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

	public static Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public static String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public static Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private static Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private static Role fromAuthority(String authority) {
		return Role.valueOf(authority.replace("ROLE_", ""));
	}

}
