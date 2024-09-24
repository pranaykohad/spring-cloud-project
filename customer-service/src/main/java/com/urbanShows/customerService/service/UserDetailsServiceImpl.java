package com.urbanShows.customerService.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.urbanShows.customerService.entity.UserInfo;
import com.urbanShows.customerService.repository.UserInfoRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserInfoRepository userInfoRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userInfoRepo.findByName(username);
		if (user != null) {
			List<String> list = new ArrayList<>();
			user.getRoles().forEach(i -> list.add(i.name()));
			return User.builder().username(user.getName()).password(user.getPassword())
					.roles(list.toArray(new String[0])).build();
		}
		throw new UsernameNotFoundException("User not found with username: " + username);
	}
}
