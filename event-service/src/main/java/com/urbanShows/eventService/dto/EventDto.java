package com.urbanShows.eventService.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventDto {

	@NotNull(message = "id cannot be null")
	private long id;

	@NotNull(message = "event name cannot be null")
	private String eventName;

	private String eventDescription;

	private LocalDateTime createdOn = LocalDateTime.now();

	private LocalDateTime bookingOpenAt = LocalDateTime.now().plusHours(24);

	private LocalDateTime bookingCloseAt;

	private boolean isBookingOpen;

	private int userMinAge = 18;

	@NotNull(message = "organizer cannot be null")
	private String organizer;

	@NotNull(message = "event type cannot be null")
	private EventTypeDto eventType;

}
