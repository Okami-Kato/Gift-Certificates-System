package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {
    private final String ID = "id";
    private final String NAME = "name";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(rs.getInt(ID), rs.getString(NAME));
    }
}
