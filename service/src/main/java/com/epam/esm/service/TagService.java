package com.epam.esm.service;

import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.impl.Service;

import java.util.List;

public interface TagService extends Service<TagDTO> {
    List<TagDTO> getAllByCertificateId(int certificateId);

    boolean nameExists(String name);
}
