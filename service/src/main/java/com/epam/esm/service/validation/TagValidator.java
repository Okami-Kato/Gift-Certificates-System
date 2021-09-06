package com.epam.esm.service.validation;

import com.epam.esm.service.dto.TagDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class TagValidator implements Validator<TagDTO> {
    private static final String NAME_REGEX = "^[\\w\\s]{1,25}$";
    private static final String NAME_MESSAGE = "Tag name must be 1-25 characters. Can contain letters, numbers and whitespaces";
    private static final String NULL_MESSAGE = "Property can't be null";
    private static final String NAME_PROPERTY = "name";

    public Optional<ConstraintViolation> validateName(String name) {
        if (name.matches(NAME_REGEX)) {
            return Optional.empty();
        } else {
            return Optional.of(new ConstraintViolation(NAME_PROPERTY, NAME_MESSAGE));
        }
    }

    @Override
    public Set<ConstraintViolation> validate(TagDTO tag) {
        Set<ConstraintViolation> violations = new HashSet<>();

        if (tag.getName() == null) {
            violations.add(new ConstraintViolation(NAME_PROPERTY, NULL_MESSAGE));
        } else {
            validateName(tag.getName()).ifPresent(violations::add);
        }

        return violations;
    }
}
