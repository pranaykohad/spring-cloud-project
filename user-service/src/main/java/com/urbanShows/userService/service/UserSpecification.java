package com.urbanShows.userService.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.urbanShows.userService.dto.SearchFilter;
import com.urbanShows.userService.entity.UserInfo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSpecification {

	public static Specification<UserInfo> buildSpecification(List<SearchFilter> filterList) {
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

	private static Predicate buildPredicate(SearchFilter criteria, Root<UserInfo> root, CriteriaBuilder criteriaBuilder) {
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

}