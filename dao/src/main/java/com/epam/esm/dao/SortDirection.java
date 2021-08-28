package com.epam.esm.dao;

public enum SortDirection {
    ASCENDING("ASC"),
    DESCENDING("DESC");
    private final String keyWord;

    SortDirection(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }
}
