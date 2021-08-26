package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDaoTestConfig.class})
class TagDaoImplTest {
    private static final Certificate CERTIFICATE = Certificate.newBuilder()
            .setName("name1")
            .setDescription("duration")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();
    private static final Tag FIRST_TAG = new Tag("test1");
    private static final Tag SECOND_TAG = new Tag("test2");

    @Autowired
    TagDao tagDao;

    @Autowired
    CertificateDao certificateDao;

    @Test
    @Transactional
    void delete() {
        int generatedId = tagDao.create(FIRST_TAG).getId();
        assertTrue(tagDao.get(generatedId).isPresent());
        assertTrue(tagDao.delete(generatedId));
        assertFalse(tagDao.get(generatedId).isPresent());
    }

    @Test
    @Transactional
    void getAllByCertificateId() {
        Tag firstTag = tagDao.create(FIRST_TAG);
        Tag secondTag = tagDao.create(SECOND_TAG);
        int certificateId = certificateDao.create(CERTIFICATE).getId();
        certificateDao.addTag(certificateId, firstTag.getId());
        certificateDao.addTag(certificateId, secondTag.getId());
        List<Tag> allByCertificateId = tagDao.getAllByCertificateId(certificateId);
        assertEquals(2, allByCertificateId.size());
        assertTrue(allByCertificateId.containsAll(Arrays.asList(firstTag, secondTag)));
    }
}