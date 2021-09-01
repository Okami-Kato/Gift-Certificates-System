package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.DaoErrorCode;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.exception.ServiceErrorCode;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final DtoMapper<Tag, TagDTO> tagDtoMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, DtoMapper<Tag, TagDTO> tagDtoMapper) {
        this.tagDao = tagDao;
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
        Tag result = null;
        try {
            result = tagDao.create(tagDtoMapper.toEntity(tag));
        } catch (DaoException e) {
            if (e.getErrorCode().equals(DaoErrorCode.DUPLICATE_KEY))
                throw new ServiceException(ServiceErrorCode.DUPLICATE_TAG_NAME);
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
    public List<TagDTO> getAllByCertificateId(int certificateId) {
        return tagDao.getAllByCertificateId(certificateId).stream().map(tagDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public boolean nameExists(String name) {
        return tagDao.nameExists(name);
    }
}
