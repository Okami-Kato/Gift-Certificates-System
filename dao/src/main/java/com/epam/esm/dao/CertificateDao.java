package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateDao extends Dao<Certificate> {
    List<Certificate> getAll(Sort sort);

    List<Certificate> getAllByNamePart(String namePart);

    List<Certificate> getAllByDescriptionPart(String descriptionPart);

    boolean addTag(int certificateId, int tagId);

    boolean removeTag(int certificateId, int tagId);

    List<Certificate> getAllByTags(Integer... ids);
}
