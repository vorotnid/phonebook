package com.phonebook.exception;

public class PhoneNumberDuplicateException extends Exception {

    @Override
    public String getMessage() {
        return "Dublicate phone number in phonebook!";
    }
}
