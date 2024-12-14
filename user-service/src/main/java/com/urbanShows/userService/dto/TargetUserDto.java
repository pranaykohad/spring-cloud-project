package com.urbanShows.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetUserDto {
	
	private AppUserInfoDto appUserInfoDto;

	private UserInfoDto systemUserInfoDto;

}
