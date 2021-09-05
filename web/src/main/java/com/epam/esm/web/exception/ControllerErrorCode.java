package com.epam.esm.web.exception;

public enum ControllerErrorCode {
    ENTITY_NOT_FOUND(40400),
    CERTIFICATE_NOT_FOUND(40401),
    TAG_NOT_FOUND(40402),
    BAD_SORT_PROPERTY(40403),
    DUPLICATE_TAG_NAME(40902),
    DUPLICATE_CERTIFICATE_TAG(40901),
    CERTIFICATE_VALIDATION_FAILURE(40301),
    TAG_VALIDATION_FAILURE(40302),
    BAD_REQUEST(400),
    SERVER_ERROR(500);
    private final int value;

    ControllerErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
