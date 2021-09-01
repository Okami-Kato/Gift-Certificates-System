package com.epam.esm.web.exception;

public class ControllerError {
    private final int errorCode;
    private final String errorMessage;

    public ControllerError(String errorMessage, ControllerErrorCode errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode.getValue();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}