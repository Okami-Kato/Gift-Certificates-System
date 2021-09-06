package com.epam.esm.web.exception;

public class ControllerException extends RuntimeException{

    private Object[] args;
    private final ControllerErrorCode errorCode;
    public ControllerException(String message, ControllerErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ControllerException(String message, ControllerErrorCode errorCode, Object... args) {
        this(message, errorCode);
        this.args = args;
    }

    public ControllerErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args;
    }
}
