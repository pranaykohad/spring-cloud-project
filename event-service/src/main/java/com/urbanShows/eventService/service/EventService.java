package com.urbanShows.eventService.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.eventService.azure.AzureBlobStorageService;
import com.urbanShows.eventService.constant.TableConfig;
import com.urbanShows.eventService.dto.ColumnConfigDto;
import com.urbanShows.eventService.dto.EventDto;
import com.urbanShows.eventService.dto.EventListDto;
import com.urbanShows.eventService.dto.EventMediaReqObject;
import com.urbanShows.eventService.dto.EventMediaRequest;
import com.urbanShows.eventService.dto.EventMediaResponse;
import com.urbanShows.eventService.dto.EventOverviewDto;
import com.urbanShows.eventService.dto.EventPage;
import com.urbanShows.eventService.dto.SearchRequest;
import com.urbanShows.eventService.entity.Event;
import com.urbanShows.eventService.entity.EventMedia;
import com.urbanShows.eventService.enums.SortOrder;
import com.urbanShows.eventService.mapper.GenericMapper;
import com.urbanShows.eventService.repository.EventRepository;
import com.urbanShows.eventService.security.exception.GenericException;

import io.jsonwebtoken.lang.Arrays;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	private final AzureBlobStorageService azureBlobStorageService;

	public List<EventMediaResponse> uploadEventMedia(List<MultipartFile> eventPhotos,
			EventMediaRequest eventMediaRequest) {
		final Optional<Event> byId = eventRepository.findById(eventMediaRequest.getEventId());
		List<EventMedia> eventMediaList = new ArrayList<>();
		if (byId.isPresent()) {
			final Event event = byId.get();

			if (!eventMediaRequest.getEventMediaReqObject().isEmpty()) {

				final List<EventMedia> existingMediaList = event.getEventMediaList();
				final List<EventMediaReqObject> newMediaList = eventMediaRequest.getEventMediaReqObject();

				for (int i = 0; i < newMediaList.size(); i++) {

					// save media in azure container
					final String fileUrl = azureBlobStorageService.uploadFile(eventPhotos.get(i));

					final EventMediaReqObject eventMediaReqObject = newMediaList.get(i);

					// match and get media is already exist in database
					final Optional<EventMedia> first = existingMediaList.stream()
							.filter(j -> j.getMediaIndex() == eventMediaReqObject.getMediaIndex()).findFirst();

					if (first.isPresent()) {
						// if match then update existing object
						final EventMedia existingEventMedia = first.get();
						existingEventMedia.setMediaUrl(fileUrl);

						// add updated media to event
						event.getEventMediaList().add(existingEventMedia);

					} else {
						// else create a new object
						final EventMedia newEventMedia = new EventMedia();
						newEventMedia.setCoverMedia(eventMediaReqObject.isCoverMedia());
						newEventMedia.setMediaIndex(eventMediaReqObject.getMediaIndex());
						newEventMedia.setMediaUrl(fileUrl);

						// add new media to event
						event.getEventMediaList().add(newEventMedia);
					}

				}
			}
			final Event save = eventRepository.save(event);
			eventMediaList = save.getEventMediaList();
		}
		final GenericMapper<EventMediaResponse, EventMedia> mapper = new GenericMapper<>(modelMapper,
				EventMediaResponse.class, EventMedia.class);
		return mapper.entityToDto(eventMediaList);
	}

	public EventListDto searchEvents(SearchRequest searchDto) {
		final Pageable pageable = buildPage(searchDto);
		final Specification<Event> spec = EventSpecification.buildSpecification(searchDto.getSearchFilters());
		final Page<Event> list = eventRepository.findAll(spec, pageable);
		return pageableEventResponse(list, spec, searchDto.getCurrentPage());
	}

	public EventOverviewDto getEventOverview(long eventId, String organizer) {
		final Event event = eventRepository.findByIdAndOrganizer(eventId, organizer);
		if (event == null) {
			throw new GenericException("Event not found or inaccessible");
		}
		final GenericMapper<EventOverviewDto, Event> mapper = new GenericMapper<>(modelMapper, EventOverviewDto.class,
				Event.class);
		return mapper.entityToDto(event);
	}

	public List<EventMediaResponse> getEventPhotos(long eventId, String organizer) {
		final Event event = eventRepository.findByIdAndOrganizer(eventId, organizer);
		if (event == null) {
			throw new GenericException("Event not found or inaccessible");
		}
		final List<EventMediaResponse> eventMediaList = new ArrayList<>();
		final GenericMapper<EventMediaResponse, EventMedia> mapper = new GenericMapper<>(modelMapper,
				EventMediaResponse.class, EventMedia.class);
		eventMediaList.addAll(mapper.entityToDto(event.getEventMediaList()));

		final List<Integer> existingIndexList = eventMediaList.stream().map(EventMediaResponse::getMediaIndex).toList();

		for (int i = 0; i < 9; i++) {
			if (!existingIndexList.contains(i)) {
				final EventMediaResponse object = new EventMediaResponse();
				object.setMediaIndex(i);
				object.setCoverMedia(i == 0);
				eventMediaList.add(object);
			}
		}

		eventMediaList.sort(Comparator.comparingInt(EventMediaResponse::getMediaIndex));

		return eventMediaList;
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

	public long saveEventOverview(EventOverviewDto eventOverview) {
		final Optional<Event> byId = eventRepository.findById(eventOverview.getId());
		Event event;
		if (byId.isPresent()) {
			event = byId.get();
			event.setEventTitle(eventOverview.getEventTitle());
			event.setEventDescription(eventOverview.getEventDescription());
			event.setUserMinAge(eventOverview.getUserMinAge());
		} else {
			event = new Event();
			event.setEventTitle(eventOverview.getEventTitle());
			event.setEventDescription(eventOverview.getEventDescription());
			event.setUserMinAge(eventOverview.getUserMinAge());
			event.setOrganizer(eventOverview.getOrganizer());
			event.setCreatedOn(LocalDateTime.now());
		}
		final Event save = eventRepository.save(event);
		return save.getId();
	}

}
