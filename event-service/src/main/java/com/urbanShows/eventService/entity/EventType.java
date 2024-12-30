package com.urbanShows.eventService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EVENT_TYPE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String eventTypeName;

	@OneToOne
    @JoinColumn(name = "event_id", nullable = false)
	private Event event;
}
