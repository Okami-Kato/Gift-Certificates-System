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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDaoTestConfig.class})
class certificateDaoTest {

    Certificate firstCertificate = Certificate.newBuilder()
            .setName("name1")
            .setDescription("duration")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();
    Certificate secondCertificate = Certificate.newBuilder()
            .setName("name2")
            .setDescription("duration")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();
    Certificate thirdCertificate = Certificate.newBuilder()
            .setName("name3")
            .setDescription("duration")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();

    Tag firstTag = new Tag("test1");
    Tag secondTag = new Tag("test2");

    @Autowired
    CertificateDao certificateDao;

    @Autowired
    TagDao tagDao;

    @Test
    @Transactional
    void delete() {
        int oldAmount = certificateDao.getAll().size();
        firstCertificate = certificateDao.create(firstCertificate);
        int newAmount = certificateDao.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        certificateDao.delete(firstCertificate.getId());
        int afterDeleteAmount = certificateDao.getAll().size();
        assertEquals(afterDeleteAmount, oldAmount);
    }

    @Test
    @Transactional
    void update() {
        firstCertificate = certificateDao.create(firstCertificate);
        Optional<Certificate> oldCertificate = certificateDao.get(firstCertificate.getId());
        assertTrue(oldCertificate.isPresent());
        firstCertificate.setName("altered name");
        certificateDao.update(firstCertificate);
        Optional<Certificate> updatedCertificate = certificateDao.get(firstCertificate.getId());
        assertTrue(updatedCertificate.isPresent());
        assertNotEquals(oldCertificate, updatedCertificate);
        oldCertificate.get().setName("altered name");
        assertEquals(oldCertificate, updatedCertificate);
    }

    @Test
    @Transactional
    void create() {
        int oldAmount = certificateDao.getAll().size();
        firstCertificate = certificateDao.create(firstCertificate);
        int newAmount = certificateDao.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        Optional<Certificate> dbCertificate = certificateDao.get(firstCertificate.getId());
        assertTrue(dbCertificate.isPresent());
        assertEquals(firstCertificate, dbCertificate.get());
    }

    @Test
    @Transactional
    void addAndRemoveTag() {
        firstTag = tagDao.create(firstTag);
        secondTag = tagDao.create(secondTag);
        firstCertificate = certificateDao.create(firstCertificate);
        secondCertificate = certificateDao.create(secondCertificate);
        thirdCertificate = certificateDao.create(thirdCertificate);
        int oldSize = certificateDao.getAllByTags(firstTag.getId()).size();
        assertEquals(oldSize, 0);
        certificateDao.addTag(firstCertificate.getId(), firstTag.getId());
        certificateDao.addTag(secondCertificate.getId(), firstTag.getId());
        certificateDao.addTag(thirdCertificate.getId(), secondTag.getId());
        int newSize = certificateDao.getAllByTags(firstTag.getId(), secondTag.getId()).size();
        assertEquals(newSize, 3);
        certificateDao.removeTag(secondCertificate.getId(), firstTag.getId());
        newSize = certificateDao.getAllByTags(firstTag.getId(), secondTag.getId()).size();
        assertEquals(newSize, 2);
    }
}