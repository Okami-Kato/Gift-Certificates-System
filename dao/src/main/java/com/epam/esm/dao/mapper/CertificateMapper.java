package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CertificateMapper implements RowMapper<Certificate> {
    private final String ID = "id";
    private final String NAME = "name";
    private final String DESCRIPTION = "description";
    private final String PRICE = "price";
    private final String DURATION = "duration";
    private final String CREATE_DATE = "create_date";
    private final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Certificate.newBuilder()
                .setId(rs.getInt(ID))
                .setName(rs.getString(NAME))
                .setDescription(rs.getString(DESCRIPTION))
                .setPrice(rs.getInt(PRICE))
                .setDuration(rs.getInt(DURATION))
                .setCreateDate(LocalDate.parse(rs.getString(CREATE_DATE)))
                .setLastUpdateDate(LocalDate.parse(rs.getString(LAST_UPDATE_DATE)))
                .build();
    }
}
