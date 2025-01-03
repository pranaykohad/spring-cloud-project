package com.urbanShows.eventService.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.eventService.constant.TableConfig;
import com.urbanShows.eventService.dto.EventListDto;
import com.urbanShows.eventService.dto.SearchRequest;
import com.urbanShows.eventService.dto.SearchFilter;
import com.urbanShows.eventService.enums.SortOrder;
import com.urbanShows.eventService.service.EventService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/event")
@AllArgsConstructor
@Slf4j
@PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_USER', 'ROLE_SUPER_ADMIN_USER', 'ROLE_ORGANIZER_USER')")
public class EventController {

	private final Environment environment;
	private final EventService eventService;

	@GetMapping("active-profile")
	public ResponseEntity<String[]> activeProfile() {
		String[] activeProfiles = environment.getActiveProfiles();
		return ResponseEntity.ok(activeProfiles);
	}

	@PostMapping("list")
	public ResponseEntity<EventListDto> getEventList(@RequestBody SearchRequest searchDto, Principal principal) {
		// check user is active or not;
		return ResponseEntity.ok(eventService.searchEvents(searchDto));
	}

}
