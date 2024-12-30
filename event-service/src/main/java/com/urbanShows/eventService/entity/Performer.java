package com.urbanShows.eventService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "PERFORMER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Performer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String performerName;

	private String description;

//	private EventMedia performerPhotoUrl;
}
