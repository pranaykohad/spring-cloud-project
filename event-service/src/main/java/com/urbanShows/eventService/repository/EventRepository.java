package com.urbanShows.eventService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.urbanShows.eventService.entity.Event;

import io.micrometer.observation.annotation.Observed;

@Repository
@Observed
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

	Page<Event> findByEventNameContainsOrOrganizerContains(String eventName, String organizer, Pageable pageable);

}
