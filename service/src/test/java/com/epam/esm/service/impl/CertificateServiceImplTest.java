package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.mapper.CertificateDtoMapper;
import com.epam.esm.service.dto.mapper.TagDtoMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CertificateServiceImplTest {
    private static final TagDTO FIRST_TAG = new TagDTO(1, "tag1");
    private static final TagDTO SECOND_TAG = new TagDTO(2, "tag2");
    private static final CertificateDTO CERTIFICATE = CertificateDTO.newBuilder()
            .setId(1)
            .setName("name")
            .setDescription("duration")
            .setDuration(10)
            .setPrice(5)
            .setCreateDate(LocalDate.now())
            .setLastUpdateDate(LocalDate.now())
            .setTags(FIRST_TAG, SECOND_TAG)
            .build();
    private static final CertificateDtoMapper certificateDtoMapper = new CertificateDtoMapper();
    private static final TagDtoMapper tagDtoMapper = new TagDtoMapper();
    private static CertificateDao certificateDao;
    private static TagDao tagDao;
    private static CertificateService certificateService;

    @BeforeAll
    static void init() {
        certificateDao = mock(CertificateDao.class);
        tagDao = mock(TagDao.class);
        certificateService = new CertificateServiceImpl(certificateDao, tagDao, certificateDtoMapper, tagDtoMapper);
    }

    @Test
    void create() {
        when(certificateDao.create(any(Certificate.class))).thenAnswer((Answer<Certificate>) invocation -> invocation.getArgument(0));

        assertEquals(CERTIFICATE, certificateService.create(CERTIFICATE));

        InOrder inOrder = inOrder(certificateDao);
        inOrder.verify(certificateDao).create(certificateDtoMapper.toEntity(CERTIFICATE));
        inOrder.verify(certificateDao).addTag(CERTIFICATE.getId(), FIRST_TAG.getId());
        inOrder.verify(certificateDao).addTag(CERTIFICATE.getId(), SECOND_TAG.getId());
    }

    @Test
    void get() {
        when(certificateDao.get(CERTIFICATE.getId())).thenReturn(Optional.of(certificateDtoMapper.toEntity(CERTIFICATE)));

        Optional<CertificateDTO> retrievedCertificate = certificateService.get(CERTIFICATE.getId());
        assertTrue(retrievedCertificate.isPresent());
        assertEquals(CERTIFICATE, retrievedCertificate.get());
        verify(certificateDao, times(1)).get(CERTIFICATE.getId());
        verify(tagDao, times(1)).getAllByCertificateId(CERTIFICATE.getId());
    }

    @Test
    void getAll() {
        CertificateDTO firstCertificate = CertificateDTO.newBuilder()
                .setId(2)
                .setName("name")
                .setDescription("description")
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .build();
        CertificateDTO secondCertificate = CertificateDTO.newBuilder()
                .setId(3)
                .setName("name")
                .setDescription("description")
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .build();
        CertificateDTO thirdCertificate = CertificateDTO.newBuilder()
                .setId(4)
                .setName("name")
                .setDescription("description")
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .build();
        List<CertificateDTO> list = Arrays.asList(firstCertificate, secondCertificate, thirdCertificate);
        when(certificateDao.getAll()).thenReturn(list.stream().map(certificateDtoMapper::toEntity).collect(Collectors.toList()));
        List<CertificateDTO> all = certificateService.getAll();
        assertEquals(all, list);

        when(certificateDao.getAllByNamePart(anyString())).thenReturn(list.stream().map(certificateDtoMapper::toEntity).collect(Collectors.toList()));
        all = certificateService.getAllByNamePart("");
        assertEquals(all, list);

        when(certificateDao.getAllByDescriptionPart(anyString())).thenReturn(list.stream().map(certificateDtoMapper::toEntity).collect(Collectors.toList()));
        all = certificateService.getAllByDescriptionPart("");
        assertEquals(all, list);

        when(certificateDao.getAllByTags(any())).thenReturn(list.stream().map(certificateDtoMapper::toEntity).collect(Collectors.toList()));
        all = certificateService.getAllByTags(1, 2, 3);
        assertEquals(all, list);

        verify(tagDao, times(4)).getAllByCertificateId(firstCertificate.getId());
        verify(tagDao, times(4)).getAllByCertificateId(secondCertificate.getId());
        verify(tagDao, times(4)).getAllByCertificateId(thirdCertificate.getId());
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
        CertificateDTO certificate = CertificateDTO.newBuilder().setId(CERTIFICATE.getId() + 1).build();
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