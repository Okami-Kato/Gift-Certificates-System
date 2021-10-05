package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.DaoError;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.mapper.DtoMapper;
import com.epam.esm.service.dto.mapper.TagDtoMapper;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validation.TagValidator;
import com.epam.esm.service.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceImplTest {
    private static final TagDTO TAG = new TagDTO(1, "tag");

    private static final DtoMapper<Tag, TagDTO> tagDtoMapper = new TagDtoMapper();
    private static final Validator<TagDTO> validator = new TagValidator();
    private static TagDao tagDao;
    private static TagService tagService;

    @BeforeAll
    static void init() {
        tagDao = mock(TagDao.class);
        tagService = new TagServiceImpl(tagDao, validator, tagDtoMapper);
    }

    @Test
    void get() {
        when(tagDao.get(TAG.getId())).thenReturn(Optional.of(tagDtoMapper.toEntity(TAG)));
        when(tagDao.get(TAG.getId() + 1)).thenReturn(Optional.empty());

        Optional<TagDTO> retrievedTag = tagService.get(TAG.getId());
        assertTrue(retrievedTag.isPresent());
        assertEquals(TAG, retrievedTag.get());
        assertFalse(tagService.get(TAG.getId() + 1).isPresent());
        verify(tagDao, times(1)).get(TAG.getId());
    }

    @Test
    void getAll() {
        TagDTO firstTag = new TagDTO(1, "tag1");
        TagDTO secondTag = new TagDTO(2, "tag2");
        TagDTO thirdTag = new TagDTO(3, "tag3");

        List<TagDTO> list = Arrays.asList(firstTag, secondTag, thirdTag);

        when(tagDao.getAll()).thenReturn(list.stream().map(tagDtoMapper::toEntity).collect(Collectors.toList()));
        List<TagDTO> all = tagService.getAll();
        assertEquals(all, list);

        when(tagDao.getAllByCertificateId(anyInt())).thenReturn(list.stream().map(tagDtoMapper::toEntity).collect(Collectors.toList()));
        all = tagService.getAllByCertificateId(1);
        assertEquals(all, list);
    }

    @Test
    void create() {
        when(tagDao.create(any(Tag.class)))
                .thenAnswer((Answer<Tag>) invocation -> invocation.getArgument(0))
                .thenThrow(new DaoException(DaoError.DUPLICATE_KEY));

        assertEquals(TAG, tagService.create(TAG));
        assertThrows(ServiceException.class, () -> tagService.create(TAG));
    }

    @Test
    void delete() {
        when(tagDao.delete(TAG.getId())).thenReturn(true).thenReturn(false);
        assertTrue(tagService.delete(TAG.getId()));
        assertFalse(tagService.delete(TAG.getId()));
        verify(tagDao, times(2)).delete(TAG.getId());
    }
}