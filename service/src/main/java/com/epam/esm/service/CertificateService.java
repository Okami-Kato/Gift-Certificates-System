package com.epam.esm.service;

import com.epam.esm.service.dto.CertificateDTO;

import java.util.List;
import java.util.Optional;

public interface CertificateService {
    Optional<CertificateDTO> get(int id);

    List<CertificateDTO> getAll();

    List<CertificateDTO> getAllByNamePart(String namePart);

    List<CertificateDTO> getAllByDescriptionPart(String descriptionPart);

    CertificateDTO create(CertificateDTO certificate);

    boolean delete(int id);

    boolean update(CertificateDTO certificate);

    boolean addTag(int certificateId, int tagId);

    boolean removeTag(int certificateId, int tagId);
}
