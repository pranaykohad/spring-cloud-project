package com.urbanShows.eventService.security.dto;

import java.util.List;

import com.urbanShows.eventService.security.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInternalInfo {
	
	private String userName;
	private String phone;
	private String password;
	private String internalPassword;
	private List<Role> roles;

}
