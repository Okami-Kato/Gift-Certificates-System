package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDAOTestConfig.class})
class CertificateDAOTest {

    Certificate certificate = Certificate.newBuilder()
            .setName("name")
            .setDescription("duration")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .build();

    @Autowired
    CertificateDAO certificateDAO;

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
        Certificate oldCertificate = certificateDAO.getEntityById(certificate.getId());
        certificate.setName("altered name");
        certificateDAO.update(certificate);
        Certificate updatedCertificate = certificateDAO.getEntityById(certificate.getId());
        assertNotEquals(oldCertificate, updatedCertificate);
        oldCertificate.setName("altered name");
        assertEquals(oldCertificate, updatedCertificate);
    }

    @Test
    @Transactional
    void create() {
        int oldAmount = certificateDAO.getAll().size();
        certificate = certificateDAO.create(certificate);
        int newAmount = certificateDAO.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        Certificate dbCertificate = certificateDAO.getEntityById(certificate.getId());
        assertEquals(certificate, dbCertificate);
    }
}