package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
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
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;
    private final TagDao tagDao;
    private final DtoMapper<Certificate, CertificateDTO> certificateDtoMapper;
    private final DtoMapper<Tag, TagDTO> tagDtoMapper;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao,
                                  TagDao tagDao,
                                  DtoMapper<Certificate, CertificateDTO> certificateDtoMapper,
                                  DtoMapper<Tag, TagDTO> tagDtoMapper) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
        this.certificateDtoMapper = certificateDtoMapper;
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public Optional<CertificateDTO> get(int id) {
        Optional<Certificate> certificate = certificateDao.get(id);
        List<TagDTO> tagList = tagDao.getAllByCertificateId(id).stream()
                .map(tagDtoMapper::toDto)
                .collect(Collectors.toList());
        return certificate.map(value -> {
            CertificateDTO dto = certificateDtoMapper.toDto(value);
            dto.getTagList().addAll(tagList);
            return dto;
        });
    }

    @Override
    public List<CertificateDTO> getAll() {
        return fillTagLists(certificateDao.getAll());
    }

    @Override
    public List<CertificateDTO> getAllByNamePart(String namePart) {
        return fillTagLists(certificateDao.getAllByNamePart(namePart));

    }

    @Override
    public List<CertificateDTO> getAllByDescriptionPart(String descriptionPart) {
        return fillTagLists(certificateDao.getAllByDescriptionPart(descriptionPart));
    }

    @Override
    public List<CertificateDTO> getAllByTags(Integer... ids) {
        return fillTagLists(certificateDao.getAllByTags(ids));
    }

    @Override
    public CertificateDTO create(CertificateDTO certificate) {
        Certificate result = certificateDao.create(certificateDtoMapper.toEntity(certificate));
        for (TagDTO tag : certificate.getTagList()) {
            if (tag.getId() != null)
                certificateDao.addTag(result.getId(), tag.getId());
        }
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
    public boolean addTag(int certificateId, int tagId) {
        return certificateDao.addTag(certificateId, tagId);
    }

    @Override
    public boolean removeTag(int certificateId, int tagId) {
        return certificateDao.removeTag(certificateId, tagId);
    }

    private List<CertificateDTO> fillTagLists(List<Certificate> certificateList){
        return certificateList.stream().map(value -> {
            List<TagDTO> tagList = tagDao.getAllByCertificateId(value.getId()).stream()
                    .map(tagDtoMapper::toDto)
                    .collect(Collectors.toList());
            CertificateDTO dto = certificateDtoMapper.toDto(value);
            dto.getTagList().addAll(tagList);
            return dto;
        }).collect(Collectors.toList());
    }
}
