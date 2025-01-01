package com.urbanShows.userService.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.service.JwtService;
import com.urbanShows.userService.util.Helper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/common")
@AllArgsConstructor
public class CommonController {

	private final JwtService jwtService;

	@GetMapping("logout")
	public ResponseEntity<Boolean> logout(HttpServletRequest request) {
		jwtService.invalidateToken(Helper.extractUserNameAndToken(request).getRight());
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("validate-token")
	public ResponseEntity<Boolean> findByToken(Principal principal, HttpServletRequest request) {
		if(principal != null) {
			return ResponseEntity.ok(jwtService.validateTokenForUserName(Helper.extractUserNameAndToken(request).getRight()
					, principal.getName()));
		}
		throw new UnauthorizedException("Unauthorized access");
	}

}
