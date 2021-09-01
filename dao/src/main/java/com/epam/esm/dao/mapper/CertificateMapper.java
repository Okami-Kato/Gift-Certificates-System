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
                .withId(rs.getInt(ID))
                .withName(rs.getString(NAME))
                .withDescription(rs.getString(DESCRIPTION))
                .withPrice(rs.getInt(PRICE))
                .withDuration(rs.getInt(DURATION))
                .withCreateDate(LocalDate.parse(rs.getString(CREATE_DATE)))
                .withLastUpdateDate(LocalDate.parse(rs.getString(LAST_UPDATE_DATE)))
                .build();
    }
}
