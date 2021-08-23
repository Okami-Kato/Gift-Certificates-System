package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(int id);

    List<T> getAll();

    T create(T t);

    boolean update(T t);

    boolean delete(int id);
}
