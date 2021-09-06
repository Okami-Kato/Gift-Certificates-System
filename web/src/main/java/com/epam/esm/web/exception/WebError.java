package com.epam.esm.web.exception;

public class WebError {
    private final int errorCode;
    private final String errorMessage;
    private Object[] args;

    public WebError(String errorExplanation, WebErrorCode errorCode) {
        this.errorMessage = errorExplanation;
        this.errorCode = errorCode.getCode();
    }

    public WebError(ControllerException e){
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