package com.phonebook.exception;

public class UserDuplicateException extends Exception {

    @Override
    public String getMessage() {
        return "User with specified username already exists!";
    }
}
