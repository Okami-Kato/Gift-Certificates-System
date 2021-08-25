package com.epam.esm.service.dto.mapper;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.dto.CertificateDTO;
import org.springframework.stereotype.Component;

@Component
public class CertificateDtoMapper implements DtoMapper<Certificate, CertificateDTO> {
    @Override
    public Certificate toEntity(CertificateDTO dto) {
        Certificate entity = Certificate.newBuilder()
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setPrice(dto.getPrice())
                .setDuration(dto.getDuration())
                .setCreateDate(dto.getCreateDate())
                .setLastUpdateDate(dto.getLastUpdateDate())
                .build();
        if (dto.getId() != null)
            entity.setId(dto.getId());
        return entity;
    }

    @Override
    public CertificateDTO toDto(Certificate entity) {
        CertificateDTO dto = new CertificateDTO();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setDuration(entity.getDuration());
        dto.setCreateDate(entity.getCreateDate());
        dto.setLastUpdateDate(entity.getLastUpdateDate());
        if (entity.getId() != null)
            dto.setId(entity.getId());
        return dto;
    }
}
