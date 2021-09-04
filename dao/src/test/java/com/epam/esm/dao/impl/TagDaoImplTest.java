package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.DaoConfig;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
@ContextConfiguration(classes = {DaoConfig.class})
class TagDaoImplTest {
    private static final Certificate CERTIFICATE = Certificate.newBuilder()
            .withName("certificate")
            .withDescription("description")
            .withDuration(10)
            .withPrice(5)
            .withCreateDate(LocalDate.now())
            .withLastUpdateDate(LocalDate.now())
            .build();
    private static final Tag FIRST_TAG = new Tag("tag1");
    private static final Tag SECOND_TAG = new Tag("tag2");

    @Autowired
    TagDao tagDao;

    @Autowired
    CertificateDao certificateDao;

    @Test
    @Transactional
    void delete() {
        int generatedId = tagDao.create(FIRST_TAG).getId();
        assertThrows(DaoException.class, () -> tagDao.create(FIRST_TAG));
        assertTrue(tagDao.idExists(generatedId));
        assertTrue(tagDao.get(generatedId).isPresent());
        assertTrue(tagDao.delete(generatedId));
        assertFalse(tagDao.get(generatedId).isPresent());
    }

    @Test
    @Transactional
    void getAllByCertificateId() {
        Tag firstTag = tagDao.create(FIRST_TAG);
        Tag secondTag = tagDao.create(SECOND_TAG);
        assertEquals(tagDao.getAll(), Arrays.asList(firstTag, secondTag));
        int certificateId = certificateDao.create(CERTIFICATE).getId();
        certificateDao.addTag(certificateId, firstTag.getId());
        certificateDao.addTag(certificateId, secondTag.getId());
        assertEquals(Arrays.asList(firstTag, secondTag), tagDao.getAllByCertificateId(certificateId));
    }
}