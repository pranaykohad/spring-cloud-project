package com.urbanShows.eventService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urbanShows.eventService.entity.EventType;

import io.micrometer.observation.annotation.Observed;

@Repository
@Observed
public interface EventTypeRepository extends JpaRepository<EventType, String> {
	

}
