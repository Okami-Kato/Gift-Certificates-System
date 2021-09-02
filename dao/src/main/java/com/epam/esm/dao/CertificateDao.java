package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.util.CertificateFilter;

import java.util.List;

public interface CertificateDao extends Dao<Certificate> {
    List<Certificate> getAll(CertificateFilter certificateFilter);

    void addTag(int certificateId, int tagId);

    boolean removeTag(int certificateId, int tagId);

    List<Certificate> getAllByTags(Integer... ids);
}
