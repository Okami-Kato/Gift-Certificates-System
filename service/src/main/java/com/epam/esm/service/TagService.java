package com.epam.esm.service;

import com.epam.esm.service.dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO get(int id);

    List<TagDTO> getAll();

    TagDTO create(TagDTO tag);

    boolean delete(int id);
}
