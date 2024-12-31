package com.urbanShows.eventService.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EVENT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String eventName;

	private String eventDescription;

	private LocalDateTime createdOn;

	private LocalDateTime bookingOpenFrom;

	private LocalDateTime bookingOpenTill;

	private boolean openBooking;

	private int userMinAge;

	private String organizer;

	@OneToOne(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private EventType eventType;
//	
//	private List<EventMedia> eventPhotos;
//	
//	private List<EventVenue> eventVenues;
//	
//	private List<EventAttribute> eventAttributes;
//	
//	private List<String> termsNConditions;
//	
//	private List<String> searchKeyWords;
//	
//	private List<Performer> performers;

}
