package com.urbanShows.eventService.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EVENT_ATTRIBUTE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAttribute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private List<Language> languages;
	
	private List<Genre> genres;

}
