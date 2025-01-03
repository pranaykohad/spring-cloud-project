package com.urbanShows.eventService.dto;

import java.util.List;

import lombok.Data;

@Data
public class EventListDto {

	private ColumnConfigDto columnConfig;
	private List<EventDto> eventList;
	private EventPage eventPage;

}
