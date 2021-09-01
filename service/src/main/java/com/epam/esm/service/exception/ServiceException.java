package com.epam.esm.service.exception;

public class ServiceException extends RuntimeException {
    private final ServiceErrorCode errorCode;

    public ServiceException(ServiceErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(ServiceErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceErrorCode getErrorCode() {
        return errorCode;
    }
}
