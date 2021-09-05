package com.epam.esm.web.exception;

public class ResourceNotFoundException extends Exception{
    private final ControllerErrorCode errorCode;
    public ResourceNotFoundException(String message, ControllerErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ControllerErrorCode getErrorCode() {
        return errorCode;
    }
}
