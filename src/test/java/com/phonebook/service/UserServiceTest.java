package com.phonebook.service;

import com.phonebook.entity.User;
import com.phonebook.exception.UserDuplicateException;
import org.junit.Assert;
import org.junit.Test;

public class UserServiceTest extends InitServiceContextBase {

    @Test(expected = UserDuplicateException.class)
    public void userAlreadyExistsTest() throws UserDuplicateException {

        userService.register(new User("stubuser", "stubuser", "stubuser", "stubuser", "stubuser"));
    }

    @Test
    public void getByUsernameTest() {
        Assert.assertEquals(mockUser, userService.findUserByUsername("stubuser"));
    }

    @Test
    public void getByIdTest() {
        Assert.assertEquals(mockUser, userService.findUserById(1L));

    }
}
