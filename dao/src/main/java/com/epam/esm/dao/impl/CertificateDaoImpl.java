package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractDao;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.exception.DaoErrorCode;
import com.epam.esm.dao.exception.DaoException;
import com.epam.esm.dao.mapper.CertificateMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.util.CertificateFilter;
import com.epam.esm.util.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CertificateDaoImpl extends AbstractDao implements CertificateDao {
    private final String SELECT_CERTIFICATE = "SELECT * FROM certificate WHERE id = ?";
    private final String SELECT_ALL_CERTIFICATES = "SELECT c.* FROM certificate c";
    private final String DELETE_CERTIFICATE = "DELETE FROM certificate WHERE id = ?";
    private final String INSERT_CERTIFICATE = "INSERT INTO certificate " +
            "(name, description, price, duration, create_date, last_update_date) values (?, ?, ?, ?, ?, ?)";
    private final String UPDATE_CERTIFICATE = "UPDATE certificate SET %s WHERE id = ?";
    private final String SET_NAME = "name = ?";
    private final String SET_DESCRIPTION = "description = ?";
    private final String SET_DURATION = "duration = ?";
    private final String SET_PRICE = "price = ?";
    private final String SET_CREATE_DATE = "create_date = ?";
    private final String SET_LAST_UPDATE_DATE = "last_update_date = ?";
    private final String ADD_TAG = "INSERT INTO certificate_tag values (?, ?)";
    private final String REMOVE_TAG = "DELETE FROM certificate_tag where certificate_id = ? and tag_id = ?";
    private final String SELECT_ALL_BY_TAGS = "SELECT DISTINCT C.* FROM certificate C INNER JOIN certificate_tag CT ON C.id = ct.certificate_id WHERE tag_id IN (%s)";
    private final String JOIN_ON_TAG_NAME = "JOIN certificate_tag ct on C.id = ct.certificate_id JOIN tag t on ct.tag_id = t.id WHERE t.name = ?";
    private final String WHERE = "WHERE 1=1";
    private final String AND_NAME_LIKE = "AND name LIKE ?";
    private final String AND_DESCRIPTION_LIKE = "AND description LIKE ?";
    private final String ORDER_BY = "ORDER BY";

    private final String TABLE_NAME = "CERTIFICATE";
    private final List<String> columns = new LinkedList<>();

    @Autowired
    public CertificateDaoImpl(DataSource dataSource) {
        super(dataSource);
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet columns = databaseMetaData.getColumns(null, null, TABLE_NAME, null);
            while (columns.next()) {
                this.columns.add(columns.getString("COLUMN_NAME").toLowerCase(Locale.ROOT));
            }
        } catch (SQLException e) {
            throw new DaoException(DaoErrorCode.DATABASE_ACCESS_ERROR, "Failed to populate list of columns");
        }
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
        StringBuilder query = new StringBuilder();
        List<Object> args = new LinkedList<>();
        if (certificate.getName() != null) {
            query.append(SET_NAME).append(",");
            args.add(certificate.getName());
        }
        if (certificate.getDescription() != null) {
            query.append(SET_DESCRIPTION).append(",");
            args.add(certificate.getDescription());
        }
        if (certificate.getDuration() != null) {
            query.append(SET_DURATION).append(",");
            args.add(certificate.getDuration());
        }
        if (certificate.getPrice() != null) {
            query.append(SET_PRICE).append(",");
            args.add(certificate.getPrice());
        }
        if (certificate.getCreateDate() != null) {
            query.append(SET_CREATE_DATE).append(",");
            args.add(certificate.getCreateDate());
        }
        if (certificate.getLastUpdateDate() != null) {
            query.append(SET_LAST_UPDATE_DATE).append(",");
            args.add(certificate.getLastUpdateDate());
        }
        query.deleteCharAt(query.length() - 1);
        args.add(certificate.getId());
        return jdbcTemplate.update(String.format(UPDATE_CERTIFICATE, query), args.toArray()) > 0;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_CERTIFICATE, id) > 0;
    }

    @Override
    public List<Certificate> getAll(CertificateFilter filter) {
        List<Object> args = new LinkedList<>();
        String query = extractQuery(filter, args);
        return jdbcTemplate.query(query, new CertificateMapper(), args.toArray());
    }

    @Override
    public void addTag(int certificateId, int tagId) {
        try {
            jdbcTemplate.update(ADD_TAG, certificateId, tagId);
        } catch (DuplicateKeyException e) {
            throw new DaoException(DaoErrorCode.DUPLICATE_KEY, String.format("Certificate (id=%s) already has tag (id=%s)", certificateId, tagId));
        }
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

    private String extractQuery(CertificateFilter filter, List<Object> args) {
        StringBuilder query = new StringBuilder(SELECT_ALL_CERTIFICATES);

        if (filter.getTagName() != null) {
            args.add(filter.getTagName());
            query.append(" ").append(JOIN_ON_TAG_NAME);
        } else if (filter.getNamePart() != null || filter.getDescriptionPart() != null) {
            query.append(" ").append(WHERE);
        }

        if (filter.getNamePart() != null) {
            args.add("%" + filter.getNamePart() + "%");
            query.append(" ").append(AND_NAME_LIKE);
        }
        if (filter.getDescriptionPart() != null) {
            args.add("%" + filter.getDescriptionPart() + "%");
            query.append(" ").append(AND_DESCRIPTION_LIKE);
        }

        if (filter.getSort() != null) {
            query.append(" ").append(extractQuery(filter.getSort()));
        }
        return query.toString();
    }

    private String extractQuery(Sort sort) {
        StringBuilder query = new StringBuilder(ORDER_BY);
        for (Sort.Order order : sort.getOrders()) {
            if (!columnExists(order.getProperty()))
                throw new DaoException(DaoErrorCode.BAD_SORT_PROPERTIES, String.format("Property (%s) doesn't exist", order.getProperty()));
            query.append(" ").append(order.getProperty()).append(" ");
            switch (order.getDirection()) {
                case ASCENDING:
                    query.append("ASC,");
                    break;
                case DESCENDING:
                    query.append("DESC,");
                    break;
            }
        }
        query.deleteCharAt(query.length() - 1); //removes last comma
        return query.toString();
    }

    private boolean columnExists(String columnName) {
        return columns.contains(columnName.toLowerCase(Locale.ROOT));
    }
}
