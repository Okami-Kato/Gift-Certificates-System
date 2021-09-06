package com.epam.esm.web.exception;

import org.springframework.http.HttpStatus;

public enum ControllerErrorCode {
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, 0),
    CERTIFICATE_NOT_FOUND(HttpStatus.NOT_FOUND, 1),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, 2),
    DUPLICATE_TAG_NAME(HttpStatus.CONFLICT, 2),
    DUPLICATE_CERTIFICATE_TAG(HttpStatus.CONFLICT, 3),
    CERTIFICATE_VALIDATION_FAILURE(HttpStatus.FORBIDDEN, 1),
    TAG_VALIDATION_FAILURE(HttpStatus.FORBIDDEN, 2),
    BAD_SORT_PROPERTY(HttpStatus.BAD_REQUEST, 1),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 2),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1);
    private final int code;
    private final HttpStatus status;

    ControllerErrorCode(HttpStatus status, int code) {
        this.status = status;
        this.code = code;
    }

    public int getCode() {
        return Integer.parseInt(status.value() + 0 + Integer.toString(code));
    }

    public HttpStatus getStatus() {
        return status;
    }
}
