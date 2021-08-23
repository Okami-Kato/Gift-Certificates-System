package com.epam.esm.dao;

import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDaoTestConfig.class})
class TagDaoTest {
    Tag tag = new Tag("test");

    @Autowired
    TagDao tagDAO;

    @Test
    @Transactional
    void delete() {
        int oldAmount = tagDAO.getAll().size();
        tag = tagDAO.create(tag);
        int newAmount = tagDAO.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        tagDAO.delete(tag.getId());
        int afterDeleteAmount = tagDAO.getAll().size();
        assertEquals(afterDeleteAmount, oldAmount);
    }

    @Test
    @Transactional
    void create() {
        int oldAmount = tagDAO.getAll().size();
        tag = tagDAO.create(tag);
        int newAmount = tagDAO.getAll().size();
        assertEquals(oldAmount + 1, newAmount);
        Optional<Tag> dbTag = tagDAO.get(tag.getId());
        assertTrue(dbTag.isPresent());
        assertEquals(tag, dbTag.get());
    }
}