package com.urbanShows.eventService.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class GenericMapper<D, E> {

	private ModelMapper mapper;
	private Class<D> dtoClass;
	private Class<E> entityClass;

	public E dtoToEntity(D dto) {
		return mapper.map(dto, entityClass);
	}

	public List<E> dtoToEntity(List<D> dtoList) {
		return dtoList.stream().map(this::dtoToEntity).toList();
	}

	public Set<E> dtoToEntity(Set<D> dtoSet) {
		return dtoSet.stream().map(this::dtoToEntity).collect(Collectors.toSet());
	}

	public D entityToDto(E entity) {
		return mapper.map(entity, dtoClass);
	}

	public List<D> entityToDto(List<E> entityList) {
		return entityList.stream().map(this::entityToDto).toList();
	}

	public Set<D> entityToDto(Set<E> entityList) {
		return entityList.stream().map(this::entityToDto).collect(Collectors.toSet());
	}

}
