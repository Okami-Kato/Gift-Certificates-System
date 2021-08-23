package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateDao extends Dao<Certificate> {
    boolean addTag(int certificateId, int tagId);

    boolean removeTag(int certificateId, int tagId);

    List<Certificate> getAllByTags(Integer... ids);
}
