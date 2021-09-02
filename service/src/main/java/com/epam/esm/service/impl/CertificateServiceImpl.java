package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.exception.DaoErrorCode;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.exception.ServiceErrorCode;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.util.CertificateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;
    private final DtoMapper<Certificate, CertificateDTO> certificateDtoMapper;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  DtoMapper<Certificate, CertificateDTO> certificateDtoMapper) {
        this.certificateDao = certificateDao;
        this.certificateDtoMapper = certificateDtoMapper;
    }

    @Override
    public Optional<CertificateDTO> get(int id) {
        Optional<Certificate> certificate = certificateDao.get(id);
        return certificate.map(certificateDtoMapper::toDto);
    }

    @Override
    public List<CertificateDTO> getAll() {
        return certificateDao.getAll().stream().map(certificateDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CertificateDTO create(CertificateDTO certificate) {
        Certificate result = certificateDao.create(certificateDtoMapper.toEntity(certificate));
        return certificateDtoMapper.toDto(result);
    }

    @Override
    public boolean delete(int id) {
        return certificateDao.delete(id);
    }

    @Override
    public boolean update(CertificateDTO certificate) {
        return certificateDao.update(certificateDtoMapper.toEntity(certificate));
    }

    @Override
    public boolean idExists(int id) {
        return certificateDao.idExists(id);
    }

    @Override
    public List<CertificateDTO> getAll(CertificateFilter certificateFilter) {
        try {
            return certificateDao.getAll(certificateFilter).stream().map(certificateDtoMapper::toDto).collect(Collectors.toList());
        } catch (DaoException e) {
            if (e.getErrorCode().equals(DaoErrorCode.BAD_SORT_PROPERTIES))
                throw new ServiceException(ServiceErrorCode.BAD_SORT_PROPERTY, e.getMessage());
            else
                throw new ServiceException(ServiceErrorCode.UNEXPECTED_ERROR, e.getMessage());
        }
    }

    @Override
    public List<CertificateDTO> getAll(Integer... ids) {
        return certificateDao.getAllByTags(ids).stream().map(certificateDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void addTag(int certificateId, int tagId) {
        try {
            certificateDao.addTag(certificateId, tagId);
        } catch (DaoException e) {
            if (e.getErrorCode().equals(DaoErrorCode.DUPLICATE_KEY)) {
                throw new ServiceException(ServiceErrorCode.DUPLICATE_CERTIFICATE_TAG);
            } else if (e.getErrorCode().equals(DaoErrorCode.CONSTRAIN_VIOLATION)) {
                throw new ServiceException(ServiceErrorCode.BAD_KEY);
            }
        }
    }

    @Override
    public boolean removeTag(int certificateId, int tagId) {
        return certificateDao.removeTag(certificateId, tagId);
    }
}
