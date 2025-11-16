package com.urbanShows.userService.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.urbanShows.userService.entity.AppUser;
import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;
import com.urbanShows.userService.repository.AppUserRepository;
import com.urbanShows.userService.repository.UserInfoRepository;
import com.urbanShows.userService.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserInfoRepository systemUserInfoRepo;

	@Autowired
	private AppUserRepository appUserInfoRepo;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		if (Helper.isPhonenumber(id)) {
			final AppUser user = appUserInfoRepo.findByPhone(id);
			if (user != null) {
				return buildUser(user.getPhone(), user.getInternalPassword(), user.getRoles());
			}
			log.error("Bad credentials or account is not active (App User): {}", id);
			throw new UsernameNotFoundException("Bad Credintials or account is not active (App User): " + id);
		} else {
			final SystemUser user = systemUserInfoRepo.findByUserNameAndStatus(id, Status.ACTIVE);
			if (user != null) {
				return buildUser(user.getUserName(), user.getPassword(), user.getRoles());
			}
			log.error("Bad credentials or account is not active (System User): {}", id);
			throw new UsernameNotFoundException("Bad Credintials or account is not active (System User): " + id);
		}
	}

	private UserDetails buildUser(String userName, String password, List<Role> roles) {
		final List<String> list = new ArrayList<>();
		roles.forEach(i -> list.add(i.name()));
		return User.builder().username(userName).password(password).roles(list.toArray(new String[0])).build();
	}

}
