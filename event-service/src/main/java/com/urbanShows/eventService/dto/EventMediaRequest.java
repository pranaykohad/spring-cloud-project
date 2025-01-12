package com.urbanShows.eventService.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventMediaRequest {

	@NotNull(message = "otp cannot be null")
	private long eventId;

	@NotNull(message = "otp cannot be null")
	private String otp;

	private List<EventMediaReqObject> eventMediaReqObject;

}
