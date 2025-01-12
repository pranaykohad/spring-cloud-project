package com.urbanShows.eventService.dto;

import lombok.Data;

@Data
public class EventMediaDto {

	private long id;

	private String mediaUrl;

	private boolean coverMedia;

	private int mediaIndex;

}
