package com.epam.esm.web.validation;

import java.util.Objects;

public class ConstraintViolation {
    private final String property;
    private final String message;

    public ConstraintViolation(String property, String message) {
        this.property = property;
        this.message = message;
    }

    public String getProperty() {
        return property;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintViolation that = (ConstraintViolation) o;
        return property.equals(that.property) && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, message);
    }

    @Override
    public String toString() {
        return "ConstraintViolation{" +
                "parameter=" + property +
                ", message='" + message + '\'' +
                '}';
    }
}
