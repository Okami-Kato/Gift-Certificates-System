package com.epam.esm.service;

import com.epam.esm.service.dto.TagDTO;

import java.util.List;

public interface TagService extends Service<TagDTO> {
    List<TagDTO> getAllByCertificateId(int certificateId);

    boolean nameExists(String name);
}
