package com.urbanShows.userService.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserInfoRespone {

	private ColumnConfigDto columnConfig;
	private List<UserInfoDto> userInfoList;
	private UserPage userPage;

}
