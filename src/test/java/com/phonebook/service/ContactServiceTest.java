package com.phonebook.service;

import com.phonebook.entity.Contact;
import com.phonebook.exception.PhoneNumberDuplicateException;
import org.junit.Assert;
import org.junit.Test;

public class ContactServiceTest extends InitServiceContextBase {


    @Test(expected = PhoneNumberDuplicateException.class)
    public void alreadyExistsTest() throws PhoneNumberDuplicateException {
        contactService.createNewContact(new Contact("stubContact", "stubContact", "stubContact", "+380(55)5555555", "", "", ""), mockUser.getUsername());
    }

    @Test
    public void addSuccessTest() throws PhoneNumberDuplicateException {
        Contact saved = contactService.createNewContact(new Contact("stubContact2", "stubContact2", "stubContact2", "+380(55)5555553", "", "", ""), mockUser.getUsername());
        Assert.assertEquals(mockContact2, saved);
    }

    @Test
    public void updateContactTest() {
        Contact edited = mockContact1;
        edited.setFirstName("edited");
        Contact retrieved = contactService.updateContact(edited);
        Assert.assertEquals(edited, retrieved);
    }
}
