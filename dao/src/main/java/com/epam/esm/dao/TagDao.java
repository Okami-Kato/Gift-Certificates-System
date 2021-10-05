package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;

public interface TagDao extends Dao<Tag> {
    List<Tag> getAllByCertificateId(int certificateId);

    boolean nameExists(String name);
}
