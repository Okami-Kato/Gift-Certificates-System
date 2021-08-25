package com.epam.esm.service.dto.mapper;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.TagDTO;
import org.springframework.stereotype.Component;

@Component
public class TagDtoMapper implements DtoMapper<Tag, TagDTO> {
    @Override
    public Tag toEntity(TagDTO dto) {
        Tag entity = new Tag();
        entity.setName(dto.getName());
        if (dto.getId() != null)
            entity.setId(dto.getId());
        return entity;
    }

    @Override
    public TagDTO toDto(Tag entity) {
        TagDTO dto = new TagDTO();
        dto.setName(entity.getName());
        if (entity.getId() != null)
            dto.setId(entity.getId());
        return dto;
    }
}
