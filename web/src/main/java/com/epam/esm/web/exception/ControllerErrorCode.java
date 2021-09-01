package com.epam.esm.web.exception;

public enum ControllerErrorCode {
    CERTIFICATE_NOT_FOUND(40401),
    TAG_NOT_FOUND(40402),
    BAD_SORT_PROPERTY(40403),
    DUPLICATE_TAG_NAME(40902),
    DUPLICATE_CERTIFICATE_TAG(40901),
    SERVER_ERROR(500);
    private final int value;

    ControllerErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
