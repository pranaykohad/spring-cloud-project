package com.urbanShows.userService.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoListDto {

	private ColumnConfig columnConfig;

	private List<UserInfoDto> userInfoList;

}
