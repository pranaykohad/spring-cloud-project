package com.urbanShows.eventService.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.eventService.dto.EventListDto;
import com.urbanShows.eventService.dto.EventMediaDto;
import com.urbanShows.eventService.dto.EventOverviewDto;
import com.urbanShows.eventService.dto.SearchFilter;
import com.urbanShows.eventService.dto.SearchRequest;
import com.urbanShows.eventService.enums.SearchOperator;
import com.urbanShows.eventService.security.authService.AuthService;
import com.urbanShows.eventService.security.enums.Role;
import com.urbanShows.eventService.security.util.Helper;
import com.urbanShows.eventService.service.EventService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/event")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_USER', 'ROLE_SUPER_ADMIN_USER', 'ROLE_ORGANIZER_USER')")
public class EventController {

	private final Environment environment;
	private final EventService eventService;
	private final AuthService authService;

	@GetMapping("active-profile")
	public ResponseEntity<String[]> activeProfile() {
		String[] activeProfiles = environment.getActiveProfiles();
		return ResponseEntity.ok(activeProfiles);
	}

	@PostMapping("list")
	public ResponseEntity<EventListDto> getEventList(@RequestBody SearchRequest searchDto,
			HttpServletRequest httpServletRequest, Principal principal) {
		final List<Role> loggedinUserRoles = Helper.getLoggedinUserRoles(principal);
		if (loggedinUserRoles.contains(Role.ORGANIZER_USER)) {
			final SearchFilter searchFilter = new SearchFilter("organizer", principal.getName(), null,
					SearchOperator.EQUALS);
			searchDto.getSearchFilters().add(searchFilter);
		}
		authService.isLogginUserActive(Helper.extractJwt(httpServletRequest));
		return ResponseEntity.ok(eventService.searchEvents(searchDto));
	}

	@GetMapping("event-overview")
	public ResponseEntity<EventOverviewDto> getEventOverview(@RequestParam long eventId, @RequestParam String organizer,
			HttpServletRequest httpServletRequest, Principal principal) {
		authService.isLogginUserActive(Helper.extractJwt(httpServletRequest));
		return ResponseEntity.ok(eventService.getEventOverview(eventId, organizer));
	}

	@PatchMapping("event-overview")
	public ResponseEntity<Long> saveEventOverview(@RequestBody EventOverviewDto eventOverview,
			HttpServletRequest httpServletRequest, Principal principal) {
		final String jwt = Helper.extractJwt(httpServletRequest);
		authService.isOrganizerActive(jwt, eventOverview.getOrganizer());
		authService.validateLoggedinUserByOtp(jwt, eventOverview.getOtp());
		return ResponseEntity.ok(eventService.saveEventOverview(eventOverview));
	}

	@GetMapping("event-photos")
	public ResponseEntity<List<EventMediaDto>> getEventPhotos(@RequestParam long eventId,
			@RequestParam String organizer, HttpServletRequest httpServletRequest, Principal principal) {
		authService.isLogginUserActive(Helper.extractJwt(httpServletRequest));
		return ResponseEntity.ok(eventService.getEventPhotos(eventId, organizer));
	}

}
