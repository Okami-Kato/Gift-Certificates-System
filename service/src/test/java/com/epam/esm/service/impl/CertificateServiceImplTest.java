package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.mapper.CertificateDtoMapper;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.util.CertificateFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CertificateServiceImplTest {
    private static final TagDTO FIRST_TAG = new TagDTO(1, "tag1");
    private static final CertificateDTO CERTIFICATE = CertificateDTO.newBuilder()
            .withId(1)
            .withName("name")
            .withDescription("duration")
            .withDuration(10)
            .withPrice(5)
            .withCreateDate(LocalDate.now())
            .withLastUpdateDate(LocalDate.now())
            .build();
    private static final DtoMapper<Certificate, CertificateDTO> certificateDtoMapper = new CertificateDtoMapper();
    private static CertificateDao certificateDao;
    private static CertificateService certificateService;

    @BeforeAll
    static void init() {
        certificateDao = mock(CertificateDao.class);
        certificateService = new CertificateServiceImpl(certificateDao, certificateDtoMapper);
    }

    @Test
    void create() {
        when(certificateDao.create(any(Certificate.class))).thenAnswer((Answer<Certificate>) invocation -> invocation.getArgument(0));

        assertEquals(CERTIFICATE, certificateService.create(CERTIFICATE));
        verify(certificateDao, times(1)).create(certificateDtoMapper.toEntity(CERTIFICATE));
    }

    @Test
    void get() {
        when(certificateDao.get(CERTIFICATE.getId())).thenReturn(Optional.of(certificateDtoMapper.toEntity(CERTIFICATE)));

        Optional<CertificateDTO> retrievedCertificate = certificateService.get(CERTIFICATE.getId());
        assertTrue(retrievedCertificate.isPresent());
        assertEquals(CERTIFICATE, retrievedCertificate.get());
        verify(certificateDao, times(1)).get(CERTIFICATE.getId());
    }

    @Test
    void getAll() {
        CertificateDTO firstCertificate = CertificateDTO.newBuilder()
                .withId(2)
                .withName("name")
                .withDescription("description")
                .withCreateDate(LocalDate.now())
                .withLastUpdateDate(LocalDate.now())
                .build();
        CertificateDTO secondCertificate = CertificateDTO.newBuilder()
                .withId(3)
                .withName("name")
                .withDescription("description")
                .withCreateDate(LocalDate.now())
                .withLastUpdateDate(LocalDate.now())
                .build();
        CertificateDTO thirdCertificate = CertificateDTO.newBuilder()
                .withId(4)
                .withName("name")
                .withDescription("description")
                .withCreateDate(LocalDate.now())
                .withLastUpdateDate(LocalDate.now())
                .build();
        List<CertificateDTO> list = Arrays.asList(firstCertificate, secondCertificate, thirdCertificate);
        when(certificateDao.getAll()).thenReturn(list.stream().map(certificateDtoMapper::toEntity).collect(Collectors.toList()));
        List<CertificateDTO> all = certificateService.getAll();
        assertEquals(all, list);

        when(certificateDao.getAll(any(CertificateFilter.class))).thenReturn(list.stream().map(certificateDtoMapper::toEntity).collect(Collectors.toList()));
        all = certificateService.getAll(new CertificateFilter());
        assertEquals(all, list);

        when(certificateDao.getAll(any(CertificateFilter.class))).thenReturn(list.stream().map(certificateDtoMapper::toEntity).collect(Collectors.toList()));
        all = certificateService.getAll(new CertificateFilter());
        assertEquals(all, list);

        when(certificateDao.getAllByTags(any())).thenReturn(list.stream().map(certificateDtoMapper::toEntity).collect(Collectors.toList()));
        all = certificateService.getAll(1, 2, 3);
        assertEquals(all, list);
    }

    @Test
    void delete() {
        when(certificateDao.delete(anyInt())).thenAnswer(invocation -> (invocation.getArgument(0) == CERTIFICATE.getId()));
        assertTrue(certificateService.delete(CERTIFICATE.getId()));
        verify(certificateDao, times(1)).delete(CERTIFICATE.getId());
        assertFalse(certificateService.delete(CERTIFICATE.getId() + 1));
    }

    @Test
    void update() {
        when(certificateDao.update(any(Certificate.class))).thenAnswer(invocation -> (((Certificate) invocation.getArgument(0)).getId().equals(CERTIFICATE.getId())));
        assertTrue(certificateService.update(CERTIFICATE));
        verify(certificateDao, times(1)).update(certificateDtoMapper.toEntity(CERTIFICATE));
        CertificateDTO certificate = CertificateDTO.newBuilder().withId(CERTIFICATE.getId() + 1).build();
        assertFalse(certificateService.update(certificate));
    }

    @Test
    void addAndRemoveTag() {
        certificateService.addTag(CERTIFICATE.getId(), FIRST_TAG.getId());
        verify(certificateDao, times(1)).addTag(CERTIFICATE.getId(), FIRST_TAG.getId());

        certificateService.removeTag(CERTIFICATE.getId(), FIRST_TAG.getId());
        verify(certificateDao, times(1)).removeTag(CERTIFICATE.getId(), FIRST_TAG.getId());
    }
}