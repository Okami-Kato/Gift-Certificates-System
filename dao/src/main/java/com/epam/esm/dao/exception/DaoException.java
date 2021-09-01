package com.epam.esm.dao.exception;

public class DaoException extends RuntimeException {
    private final DaoErrorCode errorCode;

    public DaoException(DaoErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public DaoException(DaoErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DaoErrorCode getErrorCode() {
        return errorCode;
    }
}
