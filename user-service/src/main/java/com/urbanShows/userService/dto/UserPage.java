package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.constants.TableConfig;

import lombok.Data;

@Data
public class UserPage {

	private int totalRecords;
	private int filteredRecords;
	private int totalPages;
	private int currentPage;
	private int rowStartIndex;
	private int rowEndIndex;
	private List<Integer> displayPagesIndex;
	private int recordsPerPage = TableConfig.PAGE_SIZE;

}
