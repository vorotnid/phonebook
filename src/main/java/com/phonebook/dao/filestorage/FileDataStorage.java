package com.phonebook.dao.filestorage;

public interface FileDataStorage<T> {

    T readFromFile(String filepath, Class<T> clazz);

    void writeToFile(String obj, String filepath);
}
