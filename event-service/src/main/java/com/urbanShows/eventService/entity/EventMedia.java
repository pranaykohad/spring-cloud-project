package com.urbanShows.eventService.entity;

import org.springframework.http.MediaType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EVENT_MEDIA")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventMedia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String mediaUrl;
	
	private MediaType mediaType;

	private boolean isForCover;

}
