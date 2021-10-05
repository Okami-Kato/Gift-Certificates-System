package com.epam.esm.dao;

import com.epam.esm.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends Entity> {
    Optional<T> get(int id);

    List<T> getAll();

    T create(T t);

    boolean update(T t);

    boolean delete(int id);

    boolean idExists(int id);
}
