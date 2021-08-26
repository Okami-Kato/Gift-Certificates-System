package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDao;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.mapper.CertificateMapper;
import com.epam.esm.entity.Certificate;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateDaoImpl extends AbstractDao implements CertificateDao {
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
    @Language("SQL")
    private final String ADD_TAG = "INSERT INTO certificate_tag values (?, ?)";
    @Language("SQL")
    private final String REMOVE_TAG = "DELETE FROM certificate_tag where certificate_id = ? and tag_id = ?";
    @Language("SQL")
    private final String SELECT_ALL_BY_TAGS = "SELECT DISTINCT C.* FROM certificate C INNER JOIN certificate_tag CT ON C.id = ct.certificate_id WHERE tag_id IN (%s)";
    @Language("SQL")
    private final String SELECT_ALL_BY_NAME_PART = "SELECT C.* FROM certificate C WHERE name LIKE ?";
    @Language("SQL")
    private final String SELECT_ALL_BY_DESCRIPTION_PART = "SELECT C.* FROM certificate C WHERE description LIKE ?";

    @Autowired
    public CertificateDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Certificate> get(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_CERTIFICATE, new CertificateMapper(), id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Certificate> getAll() {
        return jdbcTemplate.query(SELECT_ALL_CERTIFICATES, new CertificateMapper());
    }

    @Override
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

    @Override
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

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_CERTIFICATE, id) > 0;
    }

    @Override
    public List<Certificate> getAllByNamePart(String namePart) {
        return jdbcTemplate.query(SELECT_ALL_BY_NAME_PART, new CertificateMapper(), "%" + namePart + "%");
    }

    @Override
    public List<Certificate> getAllByDescriptionPart(String descriptionPart) {
        return jdbcTemplate.query(SELECT_ALL_BY_DESCRIPTION_PART, new CertificateMapper(), "%" + descriptionPart + "%");
    }

    @Override
    public boolean addTag(int certificateId, int tagId) {
        return jdbcTemplate.update(ADD_TAG, certificateId, tagId) > 0;
    }

    @Override
    public boolean removeTag(int certificateId, int tagId) {
        return jdbcTemplate.update(REMOVE_TAG, certificateId, tagId) > 0;
    }

    @Override
    public List<Certificate> getAllByTags(Integer... ids) {
        String inSql = String.join(",", Collections.nCopies(ids.length, "?"));

        return jdbcTemplate.query(
                String.format(SELECT_ALL_BY_TAGS, inSql),
                new CertificateMapper(), (Object[]) ids);
    }
}
