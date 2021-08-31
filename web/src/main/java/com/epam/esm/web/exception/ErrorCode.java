package com.epam.esm.web.exception;

public enum ErrorCode {
    CERTIFICATE_NOT_FOUND(40401),
    TAG_NOT_FOUND(40402),
    TAG_EXISTS(40902);
    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
