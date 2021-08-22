package com.epam.esm.dao;

import com.epam.esm.dao.mapper.CertificateMapper;
import com.epam.esm.entity.Certificate;
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
public class CertificateDAO extends AbstractDAO {
    @Language("SQL")
    private final String SELECT_CERTIFICATE = "SELECT * FROM certificate WHERE id = ?";
    @Language("SQL")
    private final String SELECT_ALL_CERTIFICATES = "SELECT * FROM certificate";
    @Language("SQL")
    private final String DELETE_CERTIFICATE = "DELETE FROM certificate WHERE id = ?";
    @Language("SQL")
    private final String INSERT_CERTIFICATE = "INSERT INTO certificate " +
            "(name, description, price, duration, create_date, last_update_date) values (?, ?, ?, ?, ?, ?)";
    @Language("SQL")
    private final String UPDATE_CERTIFICATE = "UPDATE certificate " +
            "SET name = ?, description = ?, price = ?, duration = ?, create_date = ?, last_update_date = ? WHERE id = ?";

    @Autowired
    public CertificateDAO(DataSource dataSource) {
        super(dataSource);
    }

    public Certificate getEntityById(int id) {
        return jdbcTemplate.queryForObject(SELECT_CERTIFICATE, new CertificateMapper(), id);
    }

    public List<Certificate> getAll() {
        return jdbcTemplate.query(SELECT_ALL_CERTIFICATES, new CertificateMapper());
    }

    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_CERTIFICATE, id) > 0;
    }

    public boolean update(Certificate certificate) {
        return jdbcTemplate.update(UPDATE_CERTIFICATE,
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                certificate.getId()
        ) > 0;
    }

    public Certificate create(Certificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setInt(3, certificate.getPrice());
            ps.setInt(4, certificate.getDuration());
            ps.setString(5, certificate.getCreateDate().toString());
            ps.setString(6, certificate.getLastUpdateDate().toString());
            return ps;
        }, keyHolder);
        certificate.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return certificate;
    }
}
