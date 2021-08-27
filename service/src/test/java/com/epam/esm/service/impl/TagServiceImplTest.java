package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.mapper.TagDtoMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceImplTest {
    private static final TagDTO TAG = new TagDTO(1, "tag");

    private static final TagDtoMapper tagDtoMapper = new TagDtoMapper();
    private static TagDao tagDao;
    private static TagService tagService;

    @BeforeAll
    static void init() {
        tagDao = mock(TagDao.class);
        tagService = new TagServiceImpl(tagDao, tagDtoMapper);
    }

    @Test
    void get() {
        when(tagDao.get(TAG.getId())).thenReturn(Optional.of(tagDtoMapper.toEntity(TAG)));

        Optional<TagDTO> retrievedTag = tagService.get(TAG.getId());
        assertTrue(retrievedTag.isPresent());
        assertEquals(TAG, retrievedTag.get());
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
        when(tagDao.create(any(Tag.class))).thenAnswer((Answer<Tag>) invocation -> invocation.getArgument(0));
        assertEquals(TAG, tagService.create(TAG));
    }

    @Test
    void delete() {
        when(tagDao.delete(anyInt())).thenAnswer(invocation -> (invocation.getArgument(0) == TAG.getId()));
        assertTrue(tagService.delete(TAG.getId()));
        verify(tagDao, times(1)).delete(TAG.getId());
        assertFalse(tagService.delete(TAG.getId() + 1));
    }
}