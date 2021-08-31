package com.epam.esm.service;

import com.epam.esm.dao.Sort;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.impl.Service;

import java.util.List;

public interface CertificateService extends Service<CertificateDTO> {
    List<CertificateDTO> getAll(Sort sort);

    List<CertificateDTO> getAllByNamePart(String namePart);

    List<CertificateDTO> getAllByDescriptionPart(String descriptionPart);

    List<CertificateDTO> getAllByTags(Integer... ids);

    boolean addTag(int certificateId, int tagId);

    boolean removeTag(int certificateId, int tagId);
}
