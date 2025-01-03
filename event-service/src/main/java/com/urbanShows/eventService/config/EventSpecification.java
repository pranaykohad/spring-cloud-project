package com.urbanShows.eventService.config;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.urbanShows.eventService.dto.SearchFilter;
import com.urbanShows.eventService.entity.Event;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class EventSpecification {

	public static Specification<Event> buildSpecification(List<SearchFilter> filterList) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			for (SearchFilter filter : filterList) {
				final Predicate newPredicate = buildPredicate(filter, root, criteriaBuilder);
				if (newPredicate != null) {
					predicate = criteriaBuilder.and(predicate, newPredicate);
				}
			}
			return predicate;
		};
	}

	private static Predicate buildPredicate(SearchFilter criteria, Root<Event> root, CriteriaBuilder criteriaBuilder) {
		final String key = criteria.getKey();
		final Object value = criteria.getValue();

		switch (criteria.getOperator()) {
		case EQUALS -> {
			return criteriaBuilder.equal(root.get(key), value);
		}
		case LIKE -> {
			return criteriaBuilder.like(root.get(key), "%" + value + "%");
		}
		case GT -> {
			return criteriaBuilder.greaterThan(root.get(key), (Comparable) value);
		}
		case GTE -> {
			return criteriaBuilder.greaterThanOrEqualTo(root.get(key), (Comparable) value);
		}
		case LT -> {
			return criteriaBuilder.lessThan(root.get(key), (Comparable) value);
		}
		case LTE -> {
			return criteriaBuilder.lessThanOrEqualTo(root.get(key), (Comparable) value);
		}
		case RANGE -> {
			if (criteria.getValueTo() != null) {
				return criteriaBuilder.between(root.get(key), (Comparable) value, (Comparable) criteria.getValueTo());
			}
		}
		default -> throw new IllegalArgumentException("Unsupported operation: " + criteria.getOperator());
		}
		return null;
	}

//	public static Specification<Event> hasEventName(String eventName) {
//		return (root, query, criteriaBuilder) -> StringUtils.hasText(eventName)
//				? criteriaBuilder.like(root.get("eventName"), "%" + eventName + "%")
//				: null;
//	}
//
//	public static Specification<Event> hasOrganizer(String organizer) {
//		return (root, query, criteriaBuilder) -> StringUtils.hasText(organizer)
//				? criteriaBuilder.like(root.get("organizer"), "%" + organizer + "%")
//				: null;
//	}
//
//	public static Specification<Event> isBookingOpen(boolean bookingOpen) {
//		return (root, query,
//				criteriaBuilder) -> bookingOpen ? criteriaBuilder.equal(root.get("bookingOpen"), bookingOpen) : null;
//	}

//	public static Specification<Event> bookingStartsAfter(LocalDateTime dateTime) {
//		return (root, query, criteriaBuilder) -> dateTime != null
//				? criteriaBuilder.greaterThanOrEqualTo(root.get("bookingOpenAt"), dateTime)
//				: null;
//	}
}