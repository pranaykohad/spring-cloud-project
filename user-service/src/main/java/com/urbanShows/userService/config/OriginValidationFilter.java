package com.urbanShows.userService.config;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Reject requests that do not originate from allowed browser origins.
 * Note: This does not prevent non-browser clients that set an Origin header
 * from calling the API. For stronger protection, require authentication or
 * network-level restrictions.
 */
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class OriginValidationFilter extends OncePerRequestFilter {
//
//    private static final Set<String> ALLOWED_ORIGINS = Set.of(
//    		"http://localhost:8081",
//            "http://localhost:8083",
//            "http://localhost:4200"
//    );
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String origin = request.getHeader("Origin");
//
//        // If there's no Origin header, treat it as a non-browser direct call and reject.
//        if (origin == null || !ALLOWED_ORIGINS.contains(origin)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden origin");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
