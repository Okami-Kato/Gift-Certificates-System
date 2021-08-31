package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.Sort;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.DaoConfig;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
@ContextConfiguration(classes = {DaoConfig.class})
class CertificateDaoImplTest {
    private static final Certificate FIRST_CERTIFICATE = Certificate.newBuilder()
            .setName("certificate1")
            .setDescription("description1")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();
    private static final Certificate SECOND_CERTIFICATE = Certificate.newBuilder()
            .setName("certificate2")
            .setDescription("description2")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();

    private static final Tag FIRST_TAG = new Tag("tag1");
    private static final Tag SECOND_TAG = new Tag("tag2");

    private static final String NAME_COLUMN = "name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String DURATION_COLUMN = "duration";

    @Autowired
    CertificateDao certificateDao;

    @Autowired
    TagDao tagDao;

    @Test
    void delete() {
        int generatedId = certificateDao.create(FIRST_CERTIFICATE).getId();
        assertTrue(certificateDao.get(generatedId).isPresent());
        assertTrue(certificateDao.delete(generatedId));
        assertFalse(certificateDao.get(generatedId).isPresent());
    }

    @Test
    void update() {
        Certificate certificate = certificateDao.create(FIRST_CERTIFICATE);
        certificate.setDuration(certificate.getDuration() + 5);
        assertTrue(certificateDao.update(certificate));
        Optional<Certificate> updatedCertificate = certificateDao.get(certificate.getId());
        assertTrue(updatedCertificate.isPresent());
        assertEquals(updatedCertificate.get(), certificate);
    }

    @Test
    void getByPart() {
        certificateDao.create(FIRST_CERTIFICATE);
        certificateDao.create(SECOND_CERTIFICATE);
        assertEquals(2, certificateDao.getAllByNamePart("certificate").size());
        assertEquals(1, certificateDao.getAllByNamePart("certificate1").size());
        assertEquals(0, certificateDao.getAllByNamePart("certificatee").size());

        assertEquals(2, certificateDao.getAllByDescriptionPart("description").size());
        assertEquals(1, certificateDao.getAllByDescriptionPart("description1").size());
        assertEquals(0, certificateDao.getAllByDescriptionPart("descriptionn").size());
    }

    @Test
    void getInOrder() {
        Certificate firstCertificate = Certificate.newBuilder()
                .setName("aaa")
                .setDescription("bbb")
                .setDuration(2)
                .setPrice(3)
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .build();
        Certificate secondCertificate = Certificate.newBuilder()
                .setName("bbb")
                .setDescription("ccc")
                .setDuration(1)
                .setPrice(3)
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .build();
        Certificate thirdCertificate = Certificate.newBuilder()
                .setName("ccc")
                .setDescription("aaa")
                .setDuration(3)
                .setPrice(3)
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .build();
        Certificate forthCertificate = Certificate.newBuilder()
                .setName("bbb")
                .setDescription("ddd")
                .setDuration(1)
                .setPrice(3)
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .build();

        certificateDao.create(firstCertificate);
        certificateDao.create(secondCertificate);
        certificateDao.create(thirdCertificate);

        List<Certificate> orderByNameAsc = Arrays.asList(firstCertificate, secondCertificate, thirdCertificate);
        List<Certificate> orderByDescriptionAsc = Arrays.asList(thirdCertificate, firstCertificate, secondCertificate);
        List<Certificate> orderByDurationAsc = Arrays.asList(secondCertificate, firstCertificate, thirdCertificate);

        assertEquals(orderByNameAsc, certificateDao.getAll(Sort.by(Sort.Order.asc(NAME_COLUMN))));
        assertEquals(orderByDescriptionAsc, certificateDao.getAll(Sort.by(Sort.Order.asc(DESCRIPTION_COLUMN))));
        assertEquals(orderByDurationAsc, certificateDao.getAll(Sort.by(Sort.Order.asc(DURATION_COLUMN))));

        Collections.reverse(orderByNameAsc);
        Collections.reverse(orderByDescriptionAsc);
        Collections.reverse(orderByDurationAsc);

        assertEquals(orderByNameAsc, certificateDao.getAll(Sort.by(Sort.Order.desc(NAME_COLUMN))));
        assertEquals(orderByDescriptionAsc, certificateDao.getAll(Sort.by(Sort.Order.desc(DESCRIPTION_COLUMN))));
        assertEquals(orderByDurationAsc, certificateDao.getAll(Sort.by(Sort.Order.desc(DURATION_COLUMN))));

        certificateDao.create(forthCertificate);

        List<Certificate> orderByNameAscDescriptionAsc = Arrays.asList(firstCertificate, secondCertificate, forthCertificate, thirdCertificate);
        List<Certificate> orderByNameAscDescriptionDesc = Arrays.asList(firstCertificate, forthCertificate, secondCertificate, thirdCertificate);

        assertEquals(orderByNameAscDescriptionAsc, certificateDao.getAll(Sort.by(Sort.Order.asc(NAME_COLUMN), Sort.Order.asc(DESCRIPTION_COLUMN))));
        assertEquals(orderByNameAscDescriptionDesc, certificateDao.getAll(Sort.by(Sort.Order.asc(NAME_COLUMN), Sort.Order.desc(DESCRIPTION_COLUMN))));
    }

    @Test
    void addAndRemoveTag() {
        Integer firstTagId = tagDao.create(FIRST_TAG).getId();
        Integer secondTagId = tagDao.create(SECOND_TAG).getId();
        Certificate firstCertificate = certificateDao.create(FIRST_CERTIFICATE);
        Certificate secondCertificate = certificateDao.create(SECOND_CERTIFICATE);

        certificateDao.addTag(firstCertificate.getId(), firstTagId);
        certificateDao.addTag(firstCertificate.getId(), secondTagId);
        certificateDao.addTag(secondCertificate.getId(), firstTagId);

        List<Certificate> allByTags = certificateDao.getAllByTags(firstTagId, secondTagId);
        assertEquals(Arrays.asList(firstCertificate, secondCertificate), allByTags);

        certificateDao.removeTag(secondCertificate.getId(), firstTagId);
        allByTags = certificateDao.getAllByTags(firstTagId, secondTagId);
        assertEquals(1, allByTags.size());
        assertTrue(allByTags.contains(firstCertificate));
    }
}