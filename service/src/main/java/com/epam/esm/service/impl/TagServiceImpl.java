package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.DaoError;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.exception.ServiceError;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.ConstraintViolation;
import com.epam.esm.service.validation.TagValidator;
import com.epam.esm.service.validation.Validator;
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
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final Validator<TagDTO> validator;
    private final DtoMapper<Tag, TagDTO> tagDtoMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, Validator<TagDTO> validator, DtoMapper<Tag, TagDTO> tagDtoMapper) {
        this.tagDao = tagDao;
        this.validator = validator;
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public Optional<TagDTO> get(int id) {
        Optional<Tag> tag = tagDao.get(id);
        return tag.map(tagDtoMapper::toDto);
    }

    @Override
    public List<TagDTO> getAll() {
        return tagDao.getAll().stream().map(tagDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TagDTO create(TagDTO tag) {
        Set<ConstraintViolation> violations = validator.validate(tag);
        if (!violations.isEmpty()) {
            throw new ServiceException(ServiceError.TAG_VALIDATION_FAILURE, FAILED_VALIDATION, violations.toArray());
        }
        Tag result = null;
        try {
            result = tagDao.create(tagDtoMapper.toEntity(tag));
        } catch (DaoException e) {
            if (e.getDaoError().equals(DaoError.DUPLICATE_KEY))
                throw new ServiceException(ServiceError.DUPLICATE_TAG_NAME, e.getMessage());
        }
        return tagDtoMapper.toDto(result);
    }

    @Override
    public boolean delete(int id) {
        return tagDao.delete(id);
    }

    @Override
    public boolean update(TagDTO dto) {
        throw new UnsupportedOperationException("Update operation is not supported for TagService");
    }

    @Override
    public boolean idExists(int id) {
        return tagDao.idExists(id);
    }

    @Override
    public List<TagDTO> getAllByCertificateId(int certificateId) {
        return tagDao.getAllByCertificateId(certificateId).stream().map(tagDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public boolean nameExists(String name) {
        return tagDao.nameExists(name);
    }
}
