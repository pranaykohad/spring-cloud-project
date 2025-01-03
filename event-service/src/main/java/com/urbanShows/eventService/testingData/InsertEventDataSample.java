package com.urbanShows.eventService.testingData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.urbanShows.eventService.entity.Event;
import com.urbanShows.eventService.entity.EventType;
import com.urbanShows.eventService.repository.EventRepository;
import com.urbanShows.eventService.repository.EventTypeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class InsertEventDataSample {

	private final EventRepository eventRepository;
	private final EventTypeRepository eventTypeRepository;

	private final String[] eventNames = { "Dandiya Raas", "Rock Fest", "Stand-up Comedy Night", "Food Carnival",
			"Adventure Trek", "Yoga Retreat", "Gaming Championship", "Art Exhibition", "Charity Auction", "Book Fair",
			"Wine Tasting Night", "Play Festival", "Sports Marathon", "Tech Conference", "Coding Workshop",
			"Trade Show", "Music Concert", "Cycling Tour", "Meetup Event", "Cultural Festival" };

	private final String[] organizers = { "soniya", "nilesh", "kush" };

	private final String[] descriptions = { "An exciting event for enthusiasts.", "A night of laughter and fun.",
			"The best performance in the city.", "A delightful evening for foodies.", "An unforgettable adventure.",
			"Relax and rejuvenate.", "Compete with the best gamers.", "Showcase amazing artwork.",
			"Help for a noble cause.", "Explore the latest books.", "Savor the best wines.",
			"Enjoy a theatrical delight.", "Test your endurance.", "Learn the latest tech trends.",
			"Enhance your skills in this workshop.", "Connect with industry leaders.", "Groove to the latest tunes.",
			"Pedal through scenic landscapes.", "Meet like-minded people.", "Celebrate diverse cultures." };

	private final String[] eventTypeIds = { "COMEDY_SHOW", "MISIC_SHOW", "PLAYS", "SPORTS", "ADVENTURE",
			"FOOD_AND_DRINKS", "EXIBUTIONS", "TOURS", "PERRORMANCES", "MEETUPS", "GAMING", "CONFERENCES", "WORKSHOPS",
			"CHARITY EVENTS", "TRADE SHOWS", "BOOK SIGNINGS", "WINE TASTINGS", "YOGA RETREATS", "FESTIVALS" };

	public void insertEventSampleDate() {
		IntStream.range(1, 9).forEach(i -> {
			final Event event = new Event();
			event.setEventName(eventNames[i % eventNames.length] + " #" + i);
			event.setEventDescription(descriptions[i % descriptions.length]);
			event.setCreatedOn(LocalDateTime.now());
			event.setBookingOpenAt(LocalDateTime.now().plusMinutes(i * 10));
			event.setBookingCloseAt(LocalDateTime.now().plusMinutes(i * 10 + 30));
			event.setBookingOpen(true);
			event.setUserMinAge(18 + (i % 10));
			event.setOrganizer(organizers[i % organizers.length]);

			String eventTypeId = eventTypeIds[i % eventTypeIds.length];
			Optional<EventType> eventType = eventTypeRepository.findById(eventTypeId);
			eventType.ifPresent(event::setEventType);

			System.out.println(event);
			eventRepository.save(event);
		});
	}

	public void insertEventTypeSampleDate() {
		List<EventType> eventTypes = new ArrayList<>();

		String[] eventTypeNames = { "COMEDY_SHOW", "MISIC_SHOW", "PLAYS", "SPORTS", "ADVENTURE", "FOOD_AND_DRINKS",
				"EXIBUTIONS", "TOURS", "PERRORMANCES", "MEETUPS", "GAMING", "CONFERENCES", "WORKSHOPS",
				"CHARITY EVENTS", "TRADE SHOWS", "BOOK SIGNINGS", "WINE TASTINGS", "YOGA RETREATS", "FESTIVALS" };
		for (String name : eventTypeNames) {
			final EventType eventType = new EventType();
			eventType.setEventTypeName(name);
			eventTypes.add(eventType);
			
		}
		
		eventTypeRepository.saveAll(eventTypes);
	}

}
