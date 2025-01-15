package com.urbanShows.eventService.security.filter;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.urbanShows.eventService.security.util.JwtHelper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final Pair<String, String> userNameAndToken = JwtHelper.extractUserNameAndToken(request);
		if (userNameAndToken.getLeft() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (Boolean.TRUE.equals(JwtHelper.validateToken(userNameAndToken.getRight(), userNameAndToken.getLeft()))) {
				final List<GrantedAuthority> loggedinUserRoles = JwtHelper
						.getLoggedinUserAuthorities(userNameAndToken.getRight());
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userNameAndToken.getRight(), null, loggedinUserRoles);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
