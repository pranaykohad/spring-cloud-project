package com.urbanShows.eventService.controller;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.eventService.dto.EventDto;
import com.urbanShows.eventService.service.EventService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/event")
@AllArgsConstructor
@Slf4j
public class EventController {

	private final EventService eventService;
	
	private final Environment environment;
	
	@GetMapping("active-profile")
	public ResponseEntity<String[]> activeProfile() {
		String[] activeProfiles = environment.getActiveProfiles();
		return ResponseEntity.ok(activeProfiles);
	}	

	@GetMapping("list")
	public ResponseEntity<List<EventDto>> getCustomerList() {
		return ResponseEntity.ok(null);
	} 
	
	@GetMapping("test")
	public ResponseEntity<String> test() {
		log.info("NAME TRACE: {}", "event service called");
		return ResponseEntity.ok("Pranay Kohad is my name and I am software engineer");
	}

}
