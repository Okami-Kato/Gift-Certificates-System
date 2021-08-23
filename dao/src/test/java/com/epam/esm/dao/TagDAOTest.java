package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringDAOTestConfig.class})
class TagDAOTest {
    Tag tag = new Tag("test");

    @Autowired
    TagDAO tagDAO;

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
        Tag dbTag = tagDAO.getEntityById(tag.getId());
        assertEquals(tag, dbTag);
    }
}