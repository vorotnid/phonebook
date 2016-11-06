package com.phonebook.entity;

public class DomainObject {

    private long id;

    public DomainObject(long id) {
        this.id = id;
    }

    public DomainObject() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
