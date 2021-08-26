package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.mapper.DtoMapper;
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
        Tag result = tagDao.create(tagDtoMapper.toEntity(tag));
        return tagDtoMapper.toDto(result);
    }

    @Override
    public boolean delete(int id) {
        return tagDao.delete(id);
    }
}
