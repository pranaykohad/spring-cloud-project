package com.urbanShows.eventService.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EVENT_VENUE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventVenue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private LocalDateTime startDateTime;

	private LocalDateTime endDateTime;

	private boolean isSingleDayEvent;

//	private Address venueAddress;
//
//	private List<EventSlot> eventSlot;
//
//	private List<EventSeatType> eventSeatType;

}
