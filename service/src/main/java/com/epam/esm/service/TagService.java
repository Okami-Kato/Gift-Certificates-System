package com.epam.esm.service;

import com.epam.esm.service.dto.TagDTO;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Optional<TagDTO> get(int id);

    List<TagDTO> getAll();

    List<TagDTO> getAllByCertificateId(int certificateId);

    TagDTO create(TagDTO tag);

    boolean delete(int id);
}
