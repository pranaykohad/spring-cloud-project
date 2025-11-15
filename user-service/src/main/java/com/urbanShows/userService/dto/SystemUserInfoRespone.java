package com.urbanShows.userService.dto;

import java.util.List;

import lombok.Data;

@Data
public class SystemUserInfoRespone {

	private ColumnConfigDto columnConfig;
	private List<SystemUserInfoDto> userInfoList;
	private UserPage userPage;

}
