package com.epam.esm.service.impl;

import com.epam.esm.service.dto.AbstractDTO;

import java.util.List;
import java.util.Optional;

public interface Service<T extends AbstractDTO> {
    Optional<T> get(int id);

    List<T> getAll();

    T create(T dto);

    boolean delete(int id);

    boolean update(T dto);
}
