package com.urbanShows.eventService.mapper;

import java.util.List;

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

	public D entityToDto(E entity) {
		return mapper.map(entity, dtoClass);
	}

	public List<D> entityToDto(List<E> entityList) {
		return entityList.stream().map(this::entityToDto).toList();
	}

}
