package com.epam.esm.web.exception;

public class ControllerError {
    private final int errorCode;
    private final Object errorExplanation;

    public ControllerError(Object errorExplanation, ControllerErrorCode errorCode) {
        this.errorExplanation = errorExplanation;
        this.errorCode = errorCode.getValue();
    }

    public ControllerError(ResourceNotFoundException e){
        this.errorExplanation = e.getMessage();
        this.errorCode = e.getErrorCode().getValue();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object getErrorExplanation() {
        return errorExplanation;
    }
}