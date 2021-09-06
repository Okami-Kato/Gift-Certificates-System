package com.epam.esm.service.validation;

import java.util.Set;

public interface Validator<T> {
    Set<ConstraintViolation> validate(T entity);
}
