package com.phonebook.service;

import com.phonebook.dao.ContactRepository;
import com.phonebook.dao.UserRepository;
import com.phonebook.entity.Contact;
import com.phonebook.entity.User;
import com.phonebook.exception.PhoneNumberDuplicateException;
import com.phonebook.exception.UserDuplicateException;
import com.phonebook.service.impl.ContactServiceImpl;
import com.phonebook.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.when;

public class InitServiceContextBase {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    public ContactServiceImpl contactService;

    @InjectMocks
    public UserServiceImpl userService;

    public User mockUser;
    public Contact mockContact1;
    public Contact mockContact2;
    private Contact updated;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        mockUser = new User(1L, "stubuser", "stubuser", "stubuser", "stubuser", "stubuser");
        mockContact1 = new Contact(1L, "stubContact", "stubContact", "stubContact", "+380(55)5555555", "", "", "", mockUser);
        mockContact2 = new Contact(2L, "stubContact2", "stubContact2", "stubContact2", "+380(55)5555553", "", "", "", mockUser);
        updated = new Contact(mockContact1.getId(), "edited", mockContact1.getLastName(),
                mockContact1.getMiddleName(), mockContact1.getCellPhoneNumber(), mockContact1.getHomePhoneNumber(), mockContact1.getAddress(),
                mockContact1.getEmail());
        when(userRepository.getUserByUsername(mockUser.getUsername())).thenReturn(mockUser);
        when(userRepository.findOneById(mockUser.getId())).thenReturn(mockUser);
        when(contactRepository.update(new Contact(mockContact1.getId(), "edited", mockContact1.getLastName(),
                mockContact1.getMiddleName(), mockContact1.getCellPhoneNumber(), mockContact1.getHomePhoneNumber(), mockContact1.getAddress(),
                mockContact1.getEmail()))).thenReturn(updated);
        when(contactRepository.findAllByUsername(mockUser.getUsername())).thenReturn(Arrays.asList(mockContact1, mockContact2));

        try {
            when(userRepository.add(mockUser)).thenThrow(new UserDuplicateException());
            when(contactRepository.add(mockContact2)).thenReturn(mockContact2);
            when(contactRepository.add(mockContact1)).thenThrow(new PhoneNumberDuplicateException());
        } catch (PhoneNumberDuplicateException e) {
            e.printStackTrace();
        } catch (UserDuplicateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {

    }
}
