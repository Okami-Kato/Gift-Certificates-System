package com.epam.esm.dao;

import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class TagDAO extends AbstractDAO {
    @Language("SQL")
    private final String SELECT_TAG = "SELECT * FROM tag WHERE id = ?";
    @Language("SQL")
    private final String SELECT_ALL_TAGS = "SELECT * FROM tag";
    @Language("SQL")
    private final String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    @Language("SQL")
    private final String INSERT_TAG = "INSERT INTO tag (name) values (?)";

    @Autowired
    public TagDAO(DataSource dataSource) {
        super(dataSource);
    }

    public Tag getEntityById(int id) {
        return jdbcTemplate.queryForObject(SELECT_TAG, new TagMapper(), id);
    }

    public List<Tag> getAll() {
        return jdbcTemplate.query(SELECT_ALL_TAGS, new TagMapper());
    }

    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_TAG, id) > 0;
    }

    public Tag create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_TAG, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        tag.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return tag;
    }
}
