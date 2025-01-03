package com.urbanShows.eventService.dto;

import java.util.List;

import com.urbanShows.eventService.enums.SortOrder;

import lombok.Data;

@Data
public class SearchRequest {
	
	private List<SearchFilter> searchFilters;
	private int currentPage;
	private String sortColumn;
	private SortOrder sortOrder;

}
