package com.epam.esm.entity;

public abstract class Entity {
    protected Integer id;

    public Entity(){
    }

    public Entity(int id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
