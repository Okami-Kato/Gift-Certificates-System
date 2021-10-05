package com.epam.esm.service.dto;

public abstract class AbstractDTO {
    protected Integer id;

    public AbstractDTO() {
    }

    public AbstractDTO(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
