package com.epam.esm.service.exception;

public class ServiceException extends RuntimeException {
    private final ServiceError serviceError;
    private Object[] args;

    public ServiceException(ServiceError serviceError, String message) {
        super(message);
        this.serviceError = serviceError;
    }

    public ServiceException(ServiceError serviceError, String message, Object... args) {
        this(serviceError, message);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public ServiceError getError() {
        return serviceError;
    }
}
