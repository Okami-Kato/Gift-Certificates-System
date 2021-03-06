package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.DaoErrorCode;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TagDaoImpl extends AbstractDao implements TagDao {
    private final String SELECT_TAG = "SELECT * FROM tag WHERE id = ?";
    private final String SELECT_ALL_TAGS = "SELECT * FROM tag";
    private final String SELECT_All_TAGS_BY_CERTIFICATE_ID = "SELECT T.* FROM tag T INNER JOIN certificate_tag CT on T.id = CT.tag_id where certificate_id = ?";
    private final String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    private final String INSERT_TAG = "INSERT INTO tag (name) values (?)";
    private final String NAME_EXISTS = "SELECT EXISTS(SELECT * FROM tag WHERE name = ?)";
    private final String ID_EXISTS = "SELECT EXISTS(SELECT * FROM tag WHERE id = ?)";

    @Autowired
    public TagDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Tag> get(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_TAG, new TagMapper(), id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SELECT_ALL_TAGS, new TagMapper());
    }

    @Override
    public Tag create(@NotNull Tag tag) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(INSERT_TAG, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tag.getName());
                return ps;
            }, keyHolder);
            tag.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        } catch (DuplicateKeyException e) {
            throw new DaoException(DaoErrorCode.DUPLICATE_KEY, String.format("Tag (name=%s) already exists", tag.getName()));
        }
        return tag;
    }

    @Override
    public boolean update(Tag tag) {
        throw new UnsupportedOperationException("Update operation is not supported for TagDao");
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_TAG, id) > 0;
    }

    @Override
    public boolean idExists(int id) {
        return jdbcTemplate.queryForObject(ID_EXISTS, Boolean.class, id);
    }

    @Override
    public List<Tag> getAllByCertificateId(int certificateId) {
        return jdbcTemplate.query(SELECT_All_TAGS_BY_CERTIFICATE_ID, new TagMapper(), certificateId);
    }

    @Override
    public boolean nameExists(String name) {
        return jdbcTemplate.queryForObject(NAME_EXISTS, Boolean.class, name);
    }
}
