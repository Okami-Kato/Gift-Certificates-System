package com.epam.esm.service.dto.mapper;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.dto.CertificateDTO;
import org.springframework.stereotype.Component;

@Component
public class CertificateDtoMapper implements DtoMapper<Certificate, CertificateDTO> {
    @Override
    public Certificate toEntity(CertificateDTO dto) {
        Certificate entity = Certificate.newBuilder()
                .withName(dto.getName())
                .withDescription(dto.getDescription())
                .withPrice(dto.getPrice())
                .withDuration(dto.getDuration())
                .withCreateDate(dto.getCreateDate())
                .withLastUpdateDate(dto.getLastUpdateDate())
                .build();
        if (dto.getId() != null)
            entity.setId(dto.getId());
        return entity;
    }

    @Override
    public CertificateDTO toDto(Certificate entity) {
        CertificateDTO dto = CertificateDTO.newBuilder()
                .withName(entity.getName())
                .withDescription(entity.getDescription())
                .withPrice(entity.getPrice())
                .withDuration(entity.getDuration())
                .withCreateDate(entity.getCreateDate())
                .withLastUpdateDate(entity.getLastUpdateDate())
                .build();
        if (entity.getId() != null)
            dto.setId(entity.getId());
        return dto;
    }
}
