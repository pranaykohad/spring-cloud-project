package com.urbanShows.userService.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.repository.AppUserInfoRepository;
import com.urbanShows.userService.repository.UserInfoRepository;
import com.urbanShows.userService.util.Helper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserInfoRepository systemUserInfoRepo;

	@Autowired
	private AppUserInfoRepository appUserInfoRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (Helper.isPhonenumber(username)) {
			AppUserInfo user = appUserInfoRepo.findByPhone(username);
			if (user != null) {
				List<String> list = new ArrayList<>();
				user.getRoles().forEach(i -> list.add(i.name()));
				return User.builder().username(user.getPhone()).password(user.getInternalPassword())
						.roles(list.toArray(new String[0])).build();
			}
			throw new UsernameNotFoundException("User not found with username: " + username);
		} else {
			UserInfo user = systemUserInfoRepo.findByUserName(username);
			if (user != null) {
				List<String> list = new ArrayList<>();
				user.getRoles().forEach(i -> list.add(i.name()));
				return User.builder().username(user.getUserName()).password(user.getPassword())
						.roles(list.toArray(new String[0])).build();
			}
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}
