package com.phonebook.service.impl;

import com.phonebook.dao.ContactRepository;
import com.phonebook.dao.UserRepository;
import com.phonebook.entity.Contact;
import com.phonebook.entity.User;
import com.phonebook.exception.PhoneNumberDuplicateException;
import com.phonebook.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    private final static Logger LOG = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserRepository userRepository;

    public Contact createNewContact(Contact contact, String username) throws PhoneNumberDuplicateException {

        contact.setUser(userRepository.getUserByUsername(username));
        try {
            return contactRepository.add(contact);
        } catch (Exception e) {
            throw new PhoneNumberDuplicateException();
        }
    }

    @Override
    public Contact getContactById(long contactId, String username) {
        if (env.getActiveProfiles()[0].equals("database"))
            return contactRepository.findOneById(contactId);
        else {
            return contactRepository.findAllByUsername(username).stream().filter(contact -> contact.getId() == contactId)
                    .findFirst().orElse(null);
        }
    }

    @Override
    public Contact updateContact(Contact contact) {
        return contactRepository.update(contact);
    }

    @Override
    public List<Contact> getAllContacts(String username) {
        List<Contact> contactList = contactRepository.findAllByUsername(username);
        User found = userRepository.getUserByUsername(username);
        contactList.forEach(contact -> contact.setUser(found));
        return contactList;
    }

    @Override
    public void deleteContactById(Long contactId, String username) {
        if (env.getActiveProfiles()[0].equals("database"))
            contactRepository.remove(contactRepository.findOneById(contactId));
        else {
            Contact found = contactRepository.findAllByUsername(username).stream().filter(contact -> contact.getId() == contactId)
                    .findFirst().orElse(null);
            contactRepository.remove(found);
        }
    }

    @Override
    public List<Contact> findByRequestedParameters(String firstName, String lastName, String cellPhone, String username) {
        List<Contact> contactList = contactRepository.findAllByUsername(username);
        List<Contact> filtered = null;
        if (!"".equals(firstName) && !"".equals(lastName) && !"".equals(cellPhone)) {
            filtered = contactList.stream().filter(contact -> contact.getFirstName().equals(firstName) &&
                    contact.getLastName().equals(lastName) &&
                    contact.getCellPhoneNumber().contains(cellPhone)).collect(Collectors.toList());
        } else if (!"".equals(firstName) && !"".equals(lastName) && "".equals(cellPhone)) {
            filtered = contactList.stream().filter(contact -> contact.getFirstName().equals(firstName) &&
                    contact.getLastName().equals(lastName)).collect(Collectors.toList());
        } else if (!"".equals(firstName) && "".equals(lastName) && !"".equals(cellPhone)) {
            filtered = contactList.stream().filter(contact -> contact.getFirstName().equals(firstName) &&
                    contact.getCellPhoneNumber().contains(cellPhone))
                    .collect(Collectors.toList());
        } else if ("".equals(firstName) && !"".equals(lastName) && !"".equals(cellPhone)) {
            filtered = contactList.stream().filter(contact -> contact.getLastName().equals(lastName) &&
                    contact.getCellPhoneNumber().contains(cellPhone))
                    .collect(Collectors.toList());
        } else if (!"".equals(firstName) && "".equals(lastName) && "".equals(cellPhone)) {
            filtered = contactList.stream().filter(contact -> contact.getFirstName().equals(firstName))
                    .collect(Collectors.toList());
        } else if ("".equals(firstName) && !"".equals(lastName) && "".equals(cellPhone)) {
            filtered = contactList.stream().filter(contact -> contact.getLastName().equals(lastName))
                    .collect(Collectors.toList());
        } else if ("".equals(firstName) && "".equals(lastName) && !"".equals(cellPhone)) {
            filtered = contactList.stream().filter(contact -> contact.getCellPhoneNumber().contains(cellPhone))
                    .collect(Collectors.toList());
        } else filtered = contactList;
        User user = userRepository.getUserByUsername(username);
        filtered.forEach(contact -> contact.setUser(user));
        return filtered;
    }
}
