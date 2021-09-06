package com.epam.esm.dao.exception;

public class DaoException extends RuntimeException {
    private final DaoError daoError;

    public DaoException(DaoError daoError) {
        this.daoError = daoError;
    }

    public DaoException(DaoError daoError, String message) {
        super(message);
        this.daoError = daoError;
    }

    public DaoError getDaoError() {
        return daoError;
    }
}
