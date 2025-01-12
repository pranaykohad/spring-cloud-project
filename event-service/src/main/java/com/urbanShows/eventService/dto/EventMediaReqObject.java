package com.urbanShows.eventService.dto;

import lombok.Data;

@Data
public class EventMediaReqObject {
	private long id;
	private boolean coverMedia;
	private int mediaIndex;
	private String fileName;
}
