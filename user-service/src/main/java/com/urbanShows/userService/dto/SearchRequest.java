package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.enums.SortOrder;

import lombok.Data;

@Data
public class SearchRequest {
	
	private List<SearchFilter> searchFilters;
	private int currentPage;
	private String sortColumn;
	private SortOrder sortOrder;

}
