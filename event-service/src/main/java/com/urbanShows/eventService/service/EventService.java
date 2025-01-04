package com.urbanShows.eventService.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.urbanShows.eventService.constant.TableConfig;
import com.urbanShows.eventService.dto.ColumnConfigDto;
import com.urbanShows.eventService.dto.EventDto;
import com.urbanShows.eventService.dto.EventListDto;
import com.urbanShows.eventService.dto.EventPage;
import com.urbanShows.eventService.dto.SearchRequest;
import com.urbanShows.eventService.entity.Event;
import com.urbanShows.eventService.enums.SortOrder;
import com.urbanShows.eventService.mapper.GenericMapper;
import com.urbanShows.eventService.repository.EventRepository;

import io.jsonwebtoken.lang.Arrays;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EventService {

	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;

	public EventListDto searchEvents(SearchRequest searchDto) {
		final Pageable pageable = buildPage(searchDto);
		final Specification<Event> spec = EventSpecification.buildSpecification(searchDto.getSearchFilters());
		final Page<Event> list = eventRepository.findAll(spec, pageable);
		return pageableEventResponse(list, spec, searchDto.getCurrentPage());
	}

	private Pageable buildPage(SearchRequest searchDto) {
		Pageable pageable = PageRequest.of(0, TableConfig.PAGE_SIZE);
		if (StringUtils.hasText(searchDto.getSortColumn())) {
			final Sort sort = searchDto.getSortOrder().equals(SortOrder.DESC)
					? Sort.by(searchDto.getSortColumn()).descending()
					: Sort.by(searchDto.getSortColumn()).ascending();
			pageable = PageRequest.of(searchDto.getCurrentPage(), TableConfig.PAGE_SIZE, sort);
		}
		return pageable;
	}

	private EventListDto pageableEventResponse(Page<Event> pagedEventList, Specification<Event> spec, int currentPage) {
		final GenericMapper<EventDto, Event> mapper = new GenericMapper<>(modelMapper, EventDto.class, Event.class);
		final EventListDto eventDtoList = new EventListDto();
		final ColumnConfigDto columnConfig = new ColumnConfigDto(TableConfig.EVENT_COlUMNS);
		eventDtoList.setColumnConfig(columnConfig);
		eventDtoList.setEventList(mapper.entityToDto(pagedEventList.getContent()));
		eventDtoList.setEventPage(buildEventPage(spec, currentPage, pagedEventList.getContent().size()));
		return eventDtoList;
	}

	private EventPage buildEventPage(Specification<Event> spec, int currentPage, int currentRecordSize) {
		final EventPage eventPage = new EventPage();
		final int filteredCount = getEventCount(spec);
		eventPage.setTotalPages((int) Math.ceil((double) filteredCount / TableConfig.PAGE_SIZE));
		eventPage.setFilteredRecords(filteredCount);
		eventPage.setTotalRecords(getEventCount(null));
		eventPage.setRecordsPerPage(TableConfig.PAGE_SIZE);
		eventPage.setCurrentPage(currentPage);
		final int rowStartIndex = currentPage * TableConfig.PAGE_SIZE;
		eventPage.setRowStartIndex(rowStartIndex + 1);
		eventPage.setRowEndIndex(rowStartIndex + currentRecordSize);
		eventPage
				.setDisplayPagesIndex(generateDisplayPagesIndex(eventPage.getTotalPages(), eventPage.getCurrentPage()));

		return eventPage;
	}

	private List<Integer> generateDisplayPagesIndex(int totalPages, int currentPage) {
		List<Integer> displayPagesIndex = new ArrayList<>();

		if (totalPages <= 0 || currentPage < 0 || currentPage >= totalPages) {
			final Integer[] array = { 1, 2, 3, 4, 5 };
			displayPagesIndex = new ArrayList<>(Arrays.asList(array));
		} else {
			int start = Math.max(0, Math.min(currentPage - 2, totalPages - 5));
			int end = Math.min(totalPages, start + 5);

			for (int i = start; i < end; i++) {
				displayPagesIndex.add(i);
			}
		}
		return displayPagesIndex;
	}

	private int getEventCount(Specification<Event> spec) {
		return spec != null ? (int) eventRepository.count(spec) : (int) eventRepository.count();
	}

}
