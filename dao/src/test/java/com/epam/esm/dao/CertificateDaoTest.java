package com.epam.esm.dao;

import com.epam.esm.dao.impl.CertificateDao;
import com.epam.esm.entity.Certificate;
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
class CertificateDaoTest {

    Certificate certificate = Certificate.newBuilder()
            .setName("name")
            .setDescription("duration")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();

    @Autowired
    CertificateDao certificateDAO;

    @Test
    @Transactional
    void delete() {
        int oldAmount = certificateDAO.getAll().size();
        certificate = certificateDAO.create(certificate);
        int newAmount = certificateDAO.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        certificateDAO.delete(certificate.getId());
        int afterDeleteAmount = certificateDAO.getAll().size();
        assertEquals(afterDeleteAmount, oldAmount);
    }

    @Test
    @Transactional
    void update() {
        certificate = certificateDAO.create(certificate);
        Optional<Certificate> oldCertificate = certificateDAO.get(certificate.getId());
        assertTrue(oldCertificate.isPresent());
        certificate.setName("altered name");
        certificateDAO.update(certificate);
        Optional<Certificate> updatedCertificate = certificateDAO.get(certificate.getId());
        assertTrue(updatedCertificate.isPresent());
        assertNotEquals(oldCertificate, updatedCertificate);
        oldCertificate.get().setName("altered name");
        assertEquals(oldCertificate, updatedCertificate);
    }

    @Test
    @Transactional
    void create() {
        int oldAmount = certificateDAO.getAll().size();
        certificate = certificateDAO.create(certificate);
        int newAmount = certificateDAO.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        Optional<Certificate> dbCertificate = certificateDAO.get(certificate.getId());
        assertTrue(dbCertificate.isPresent());
        assertEquals(certificate, dbCertificate.get());
    }
}