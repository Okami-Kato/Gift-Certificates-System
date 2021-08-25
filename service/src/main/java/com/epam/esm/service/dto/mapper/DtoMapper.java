package com.epam.esm.service.dto.mapper;

public interface DtoMapper<T, K> {
    T toEntity(K dto);
    K toDto(T entity);
}
