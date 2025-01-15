package com.urbanShows.eventService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.urbanShows.eventService.entity.EventMedia;

import io.micrometer.observation.annotation.Observed;

@Repository
@Observed
public interface EventMediaRepository extends JpaRepository<EventMedia, Long> {

	@Modifying
	@Query(value = "DELETE FROM event_media WHERE event_id = :eventId", nativeQuery = true)
	void deleteByEventId(@Param("eventId") Long eventId);

}
