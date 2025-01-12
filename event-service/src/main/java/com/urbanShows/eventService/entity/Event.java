package com.urbanShows.eventService.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	private String eventTitle;

	private String eventDescription;

	private LocalDateTime createdOn;

	private LocalDateTime bookingOpenAt;

	private LocalDateTime bookingCloseAt;

	private boolean bookingOpen;

	private int userMinAge;

	private String organizer;

	@ManyToOne
	@JoinColumn(name = "event_type_id")
	private EventType eventType;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "event_id", referencedColumnName = "id")
	private List<EventMedia> eventMediaList;
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
