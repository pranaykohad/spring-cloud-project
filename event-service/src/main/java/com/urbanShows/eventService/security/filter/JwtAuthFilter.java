package com.urbanShows.eventService.security.filter;

import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.urbanShows.eventService.security.authService.AuthService;
import com.urbanShows.eventService.security.exception.JwtParseException;
import com.urbanShows.eventService.security.service.UserDetailsServiceImpl;
import com.urbanShows.eventService.security.util.Helper;

import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.Tuple;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private UserDetailsServiceImpl userDetailsService;

	private AuthService authService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final Pair<String, String> userNameAndToken = Helper.extractUserNameAndToken(request);
		if (userNameAndToken.getLeft() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userNameAndToken.getLeft());
			if (Boolean.TRUE.equals(authService.validateToken(userNameAndToken.getRight()))) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
