package com.epam.esm.web.exception;

public class ControllerError {
    private final int errorCode;
    private final String errorMessage;
    private Object[] args;

    public ControllerError(String errorExplanation, ControllerErrorCode errorCode) {
        this.errorMessage = errorExplanation;
        this.errorCode = errorCode.getCode();
    }

    public ControllerError(ControllerException e){
        this.errorMessage = e.getMessage();
        this.errorCode = e.getErrorCode().getCode();
        this.args = e.getArgs();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object[] getArgs() {
        return args;
    }
}