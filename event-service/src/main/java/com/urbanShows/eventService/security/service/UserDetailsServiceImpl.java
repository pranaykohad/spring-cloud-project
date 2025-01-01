package com.urbanShows.eventService.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.urbanShows.eventService.dto.UserInfoDto;
import com.urbanShows.eventService.security.authService.AuthService;
import com.urbanShows.eventService.security.dto.UserInternalInfo;
import com.urbanShows.eventService.security.enums.Role;
import com.urbanShows.eventService.security.util.Helper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private AuthService authService;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		if (Helper.isPhonenumber(id)) {
			String authHeader = httpServletRequest.getHeader("Authorization");
			String token = null;
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				id = Helper.extractUsername(token);
				final UserInternalInfo userInfo = authService.getLoggedinAppUserInfo(token);
				if (userInfo != null) {
					return buildUser(userInfo.getPhone(), userInfo.getInternalPassword(), userInfo.getRoles());
				}
			}
			throw new UsernameNotFoundException("User not found with phone: " + id);
		} else {
			String authHeader = httpServletRequest.getHeader("Authorization");
			String token = null;
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				id = Helper.extractUsername(token);
				final UserInternalInfo userInfo = authService.getLoggedinSystemUserInfo(token);
				if (userInfo != null) {
					return buildUser(userInfo.getUserName(), userInfo.getPassword(), userInfo.getRoles());
				}
			}

			throw new UsernameNotFoundException("User not found with username: " + id);
		}
	}

	private UserDetails buildUser(String userName, String password, List<Role> roles) {
		final List<String> list = new ArrayList<>();
		roles.forEach(i -> list.add(i.name()));
		return User.builder().username(userName).password(password).roles(list.toArray(new String[0])).build();
	}

}
