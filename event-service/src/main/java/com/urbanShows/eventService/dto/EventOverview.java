package com.urbanShows.eventService.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.urbanShows.eventService.entity.EventMedia;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventOverview {

	@NotNull(message = "id cannot be null")
	private long id;
	
	@NotNull(message = "event title cannot be null")
	private String eventTitle;
	
	private String eventDescription;
	
	private LocalDateTime createdOn = LocalDateTime.now();
	
	@NotNull(message = "organizer cannot be null")
	private String organizer;	
	
	private int userMinAge = 0;
	
	private List<EventMedia> eventPhotos;
}
