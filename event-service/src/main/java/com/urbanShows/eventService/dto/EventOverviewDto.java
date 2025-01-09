package com.urbanShows.eventService.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventOverviewDto {

	private long id;
	
	@NotNull(message = "user name cannot be null")
	private String userName;
	
	@NotNull(message = "otp cannot be null")
	private String otp;

	@NotNull(message = "event title cannot be null")
	private String eventTitle;

	private String eventDescription;

	private LocalDateTime createdOn = LocalDateTime.now();

	@NotNull(message = "organizer cannot be null")
	private String organizer;

	private int userMinAge = 0;

}
