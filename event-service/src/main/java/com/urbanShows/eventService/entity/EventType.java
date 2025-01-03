package com.urbanShows.eventService.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "EVENT_TYPE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventType {

	@Id
	private String eventTypeName;

//	@OneToOne(mappedBy = "eventType")
//	@ToString.Exclude
//	private Event event;
	
//	@OneToMany(mappedBy = "eventType", cascade = CascadeType.ALL, orphanRemoval = true)
//	@OneToMany
//    private List<Event> events;

}
