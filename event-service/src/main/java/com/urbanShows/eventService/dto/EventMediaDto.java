package com.urbanShows.eventService.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class EventMediaDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long eventId;

	private String mediaUrl;

	private MultipartFile mediaFile;

	private boolean isForCover;
	
	private int index;

}
