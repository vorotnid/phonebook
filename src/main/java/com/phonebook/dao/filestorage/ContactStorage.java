package com.phonebook.dao.filestorage;

import com.phonebook.entity.Contact;

import java.util.List;

class ContactStorage {

    private List<Contact> contactList;

    public ContactStorage(List<Contact> contactList) {
        this.contactList = contactList;
    }

    ContactStorage() {
    }

    List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
