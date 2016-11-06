package com.phonebook.service;

import com.phonebook.entity.User;
import com.phonebook.exception.UserDuplicateException;

public interface UserService {

    User register(User user) throws UserDuplicateException;

    User findUserByUsername(String username);

    User findUserById(Long userId);
}
