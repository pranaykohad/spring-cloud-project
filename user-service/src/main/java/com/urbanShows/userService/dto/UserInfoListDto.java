package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.config.TableConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoListDto {

	private ColumnConfig columnConfig;
	private List<UserInfoDto> userInfoList;
	private int totalRecords;
	private int totalPages;
	private int recordsPerPage = TableConfig.PAGE_SIZE;

}
