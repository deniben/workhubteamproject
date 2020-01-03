package com.spring.utils.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface DTOMapper<E, D> {
	E toEntity(D dto);

	D toDto(E entity);

	default List<D> toDTOs(List<E> entities){
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}
}
