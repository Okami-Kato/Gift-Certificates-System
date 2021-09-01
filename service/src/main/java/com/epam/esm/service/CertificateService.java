package com.epam.esm.service;

import com.epam.esm.filter.CertificateFilter;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.impl.Service;

import java.util.List;

public interface CertificateService extends Service<CertificateDTO> {
    List<CertificateDTO> getAll(CertificateFilter certificateFilter);

    List<CertificateDTO> getAll(Integer... tagIds);

    void addTag(int certificateId, int tagId);

    boolean removeTag(int certificateId, int tagId);
}
