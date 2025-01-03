package com.urbanShows.eventService.dto;

import java.util.List;

import com.urbanShows.eventService.constant.TableConfig;

import lombok.Data;

@Data
public class EventPage {

	private int totalRecords;
	private int filteredRecords;
	private int totalPages;
	private int currentPage;
	private int rowStartIndex;
	private int rowEndIndex;
	private List<Integer> displayPagesIndex;
	private int recordsPerPage = TableConfig.PAGE_SIZE;

}
