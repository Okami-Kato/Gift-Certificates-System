package com.epam.esm.web.exception;

public class ControllerException extends RuntimeException{

    private Object[] args;
    private final WebErrorCode errorCode;
    public ControllerException(String message, WebErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ControllerException(String message, WebErrorCode errorCode, Object... args) {
        this(message, errorCode);
        this.args = args;
    }

    public WebErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args;
    }
}
