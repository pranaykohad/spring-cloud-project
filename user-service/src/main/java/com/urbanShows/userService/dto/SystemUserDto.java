package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserDto {

	private String displayName;

	private List<Role> roles;

	private String jwt;
	
	private String profilePicUrl;
	
	private String userName;

}
