package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDaoTestConfig.class})
class TagDaoImplTest {
    Certificate certificate = Certificate.newBuilder()
            .setName("name1")
            .setDescription("duration")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();
    Tag firstTag = new Tag("test1");
    Tag secondTag = new Tag("test2");

    @Autowired
    TagDao tagDao;

    @Autowired
    CertificateDao certificateDao;

    @Test
    @Transactional
    void delete() {
        int oldAmount = tagDao.getAll().size();
        firstTag = tagDao.create(firstTag);
        int newAmount = tagDao.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        assertTrue(tagDao.delete(firstTag.getId()));
        assertFalse(tagDao.delete(firstTag.getId()));
        int afterDeleteAmount = tagDao.getAll().size();
        assertEquals(afterDeleteAmount, oldAmount);
    }

    @Test
    @Transactional
    void create() {
        int oldAmount = tagDao.getAll().size();
        firstTag = tagDao.create(firstTag);
        int newAmount = tagDao.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        assertThrows(DataAccessException.class, () -> tagDao.create(firstTag));
        Optional<Tag> dbTag = tagDao.get(firstTag.getId());
        assertTrue(dbTag.isPresent());
        assertEquals(firstTag, dbTag.get());
    }

    @Test
    @Transactional
    void getAllByCertificateId() {
        firstTag = tagDao.create(firstTag);
        secondTag = tagDao.create(secondTag);
        certificate = certificateDao.create(certificate);
        certificateDao.addTag(certificate.getId(), firstTag.getId());
        certificateDao.addTag(certificate.getId(), secondTag.getId());
        List<Tag> allByCertificateId = tagDao.getAllByCertificateId(certificate.getId());
        assertEquals(allByCertificateId.size(), 2);
        assertTrue(allByCertificateId.containsAll(Arrays.asList(firstTag, secondTag)));
    }
}