package com.phonebook.dao;

import com.phonebook.entity.Contact;

import java.util.List;

public interface ContactRepository extends DomainObjectRepository<Contact> {

    List<Contact> findAllByUsername(String username);
}
