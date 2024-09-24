package com.urbanShows.eventService.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.eventService.dto.EventDto;
import com.urbanShows.eventService.service.EventService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("customer")
@AllArgsConstructor
@Slf4j
public class EventController {

	private EventService eventService;

	@GetMapping("list")
	public ResponseEntity<List<EventDto>> getCustomerList() {
		return ResponseEntity.ok(null);
	} 
	
	@GetMapping("test")
	public ResponseEntity<String> test() {
		log.info("event service api test is called");
		return ResponseEntity.ok("Pranay Kohad is my name");
	}

}
