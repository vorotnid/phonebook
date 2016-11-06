package com.phonebook.dao.filestorage;

import com.google.gson.Gson;
import com.phonebook.dao.ContactRepository;
import com.phonebook.entity.Contact;
import com.phonebook.exception.PhoneNumberDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Profile("fileStorage")
@Repository
public class JsonFileContactRepository implements ContactRepository {

    @Autowired
    private Gson gson;

    @Autowired
    private FileDataStorage<ContactStorage> fileDataStorage;
    private final static String FILE_DIRECTORY = File.separator + "contacts" + File.separator;

    @Override
    public List<Contact> findAllByUsername(String username) {
        String filename = username + "_contacts.txt";
        ContactStorage storage = fileDataStorage.readFromFile(FILE_DIRECTORY + filename, ContactStorage.class);
        if (storage != null) return storage.getContactList();
        return new ArrayList<>();
    }

    @Override
    public Contact add(Contact domainObject) throws PhoneNumberDuplicateException {
        String filename = domainObject.getUser().getUsername() + "_contacts.txt";
        List<Contact> contactList;
        ContactStorage contactStorage = fileDataStorage.readFromFile(FILE_DIRECTORY + filename, ContactStorage.class);
        if (contactStorage != null && contactStorage.getContactList().size() != 0) {
            contactList = contactStorage.getContactList();
            if (contactList.stream().anyMatch(contact ->
                    contact.getCellPhoneNumber().equals(domainObject.getCellPhoneNumber()))) {
                throw new PhoneNumberDuplicateException();
            }
            long lastId = contactList.get(contactList.size() - 1).getId();
            domainObject.setId(lastId + 1);
            contactList.add(domainObject);
        } else {
            contactList = new LinkedList<>();
            domainObject.setId(1);
            contactList.add(domainObject);
            contactStorage = new ContactStorage(contactList);
        }
        String jsonStr = gson.toJson(contactStorage);
        fileDataStorage.writeToFile(jsonStr, FILE_DIRECTORY + filename);
        return domainObject;
    }

    @Override
    public void remove(Contact domainObject) {
        String filename = domainObject.getUser().getUsername() + "_contacts.txt";
        ContactStorage storage = fileDataStorage.readFromFile(FILE_DIRECTORY + filename, ContactStorage.class);
        storage.getContactList().remove(domainObject);
        fileDataStorage.writeToFile(gson.toJson(storage), FILE_DIRECTORY + filename);
    }

    @Override
    public Contact update(Contact domainObject) {
        String filename = domainObject.getUser().getUsername() + "_contacts.txt";
        ContactStorage storage = fileDataStorage.readFromFile(FILE_DIRECTORY + filename, ContactStorage.class);
        Contact found = storage.getContactList().stream().filter(contact -> contact.getId() == domainObject.getId())
                .findFirst().orElse(null);
        found.setFirstName(domainObject.getFirstName());
        found.setLastName(domainObject.getLastName());
        found.setMiddleName(domainObject.getMiddleName());
        found.setCellPhoneNumber(domainObject.getCellPhoneNumber());
        found.setHomePhoneNumber(domainObject.getHomePhoneNumber());
        found.setAddress(domainObject.getAddress());
        found.setEmail(domainObject.getEmail());
        fileDataStorage.writeToFile(gson.toJson(storage), FILE_DIRECTORY + filename);
        return found;
    }

    @Override
    public Contact findOneById(long id) {
        return null;
    }
}
