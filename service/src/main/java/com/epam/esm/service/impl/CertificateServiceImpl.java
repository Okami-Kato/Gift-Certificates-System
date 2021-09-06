package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.exception.DaoError;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.exception.ServiceError;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.CertificateValidator;
import com.epam.esm.service.validation.ConstraintViolation;
import com.epam.esm.service.validation.Validator;
import com.epam.esm.util.CertificateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.service.exception.ErrorMessage.FAILED_VALIDATION;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;
    private final Validator<CertificateDTO> validator;
    private final DtoMapper<Certificate, CertificateDTO> certificateDtoMapper;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  Validator<CertificateDTO> validator,
                                  DtoMapper<Certificate, CertificateDTO> certificateDtoMapper) {
        this.certificateDao = certificateDao;
        this.validator = validator;
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
        Set<ConstraintViolation> violations = validator.validate(certificate);
        if (!violations.isEmpty()) {
            throw new ServiceException(
                    ServiceError.CERTIFICATE_VALIDATION_FAILURE, FAILED_VALIDATION, violations.toArray()
            );
        }
        Certificate result = certificateDao.create(certificateDtoMapper.toEntity(certificate));
        return certificateDtoMapper.toDto(result);
    }

    @Override
    public boolean delete(int id) {
        return certificateDao.delete(id);
    }

    @Override
    public boolean update(CertificateDTO certificate) {
        Set<ConstraintViolation> violations = validator.validate(certificate);
        if (!violations.isEmpty()) {
            throw new ServiceException(ServiceError.CERTIFICATE_VALIDATION_FAILURE, FAILED_VALIDATION, violations.toArray());
        }
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
            if (e.getDaoError().equals(DaoError.BAD_SORT_PROPERTIES))
                throw new ServiceException(ServiceError.BAD_SORT_PROPERTY, e.getMessage());
            else
                throw new ServiceException(ServiceError.UNEXPECTED_ERROR, e.getMessage());
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
            if (e.getDaoError().equals(DaoError.DUPLICATE_KEY)) {
                throw new ServiceException(ServiceError.DUPLICATE_CERTIFICATE_TAG, e.getMessage());
            } else if (e.getDaoError().equals(DaoError.CONSTRAIN_VIOLATION)) {
                throw new ServiceException(ServiceError.BAD_KEY, e.getMessage());
            }
        }
    }

    @Override
    public boolean removeTag(int certificateId, int tagId) {
        return certificateDao.removeTag(certificateId, tagId);
    }
}
