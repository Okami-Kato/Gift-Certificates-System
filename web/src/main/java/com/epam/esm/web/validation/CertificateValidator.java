package com.epam.esm.web.validation;

import com.epam.esm.service.dto.CertificateDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class CertificateValidator {
    private static final String NAME_REGEX = "^[\\w\\s.,?!@'#$:;*+-=%]{1,50}$";

    private static final String NAME_MESSAGE = "Certificate name must be 1-50 characters. Can contain letters, numbers and special characters .,?!@'#$:;*+-=%";
    private static final String PRICE_MESSAGE = "Certificate price must be a positive integer";
    private static final String DURATION_MESSAGE = "Certificate duration must be a positive integer";
    private static final String NULL_MESSAGE = "Property can't be null";

    private static final String NAME_PROPERTY = "name";
    private static final String DESCRIPTION_PROPERTY = "description";
    private static final String PRICE_PROPERTY = "price";
    private static final String DURATION_PROPERTY = "duration";

    public Optional<ConstraintViolation> validateName(String name) {
        if (name == null) {
            return Optional.of(new ConstraintViolation(NAME_PROPERTY, NULL_MESSAGE));
        }
        if (name.matches(NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(NAME_PROPERTY, NAME_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validatePrice(Integer price) {
        if (price == null) {
            return Optional.of(new ConstraintViolation(PRICE_PROPERTY, NULL_MESSAGE));
        }
        if (price > 0) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(PRICE_PROPERTY, PRICE_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateDuration(Integer duration) {
        if (duration == null) {
            return Optional.of(new ConstraintViolation(DURATION_PROPERTY, NULL_MESSAGE));
        }
        if (duration > 0) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(DURATION_PROPERTY, DURATION_MESSAGE));
        }
    }

    public Optional<ConstraintViolation> validateDescription(String description) {
        if (description == null) {
            return Optional.of(new ConstraintViolation(DESCRIPTION_PROPERTY, NULL_MESSAGE));
        } else {
            return Optional.empty();
        }
    }

    public Set<ConstraintViolation> validateCertificate(CertificateDTO certificate) {
        Set<ConstraintViolation> violations = new HashSet<>();
        validateName(certificate.getName()).ifPresent(violations::add);
        validatePrice(certificate.getPrice()).ifPresent(violations::add);
        validateDuration(certificate.getDuration()).ifPresent(violations::add);
        validateDescription(certificate.getDescription()).ifPresent(violations::add);

        return violations;
    }
}
