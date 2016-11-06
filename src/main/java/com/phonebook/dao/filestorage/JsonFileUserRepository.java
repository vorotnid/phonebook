package com.phonebook.dao.filestorage;

import com.google.gson.Gson;
import com.phonebook.dao.UserRepository;
import com.phonebook.entity.User;
import com.phonebook.exception.UserDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Profile("fileStorage")
@Repository
public class JsonFileUserRepository implements UserRepository {

    @Autowired
    private Gson gson;

    @Autowired
    FileDataStorage<UserStorage> fileDataStorage;

    private static final String FILENAME = File.separator + "users" + File.separator + "users.txt";

    @Override
    public User getUserByUsername(String username) {
        UserStorage storage = fileDataStorage.readFromFile(FILENAME, UserStorage.class);
        User found = storage.getUsers().stream().filter(user -> user.getUsername().equals(username))
                .findFirst().orElse(null);
        if (found != null) return found;
        return new User();
    }

    @Override
    public User add(User domainObject) throws UserDuplicateException {
        UserStorage storage = fileDataStorage.readFromFile(FILENAME, UserStorage.class);
        if (storage != null && storage.getUsers().size() != 0) {
            ArrayList<User> userList = (ArrayList<User>) storage.getUsers();
            if (userList.stream().anyMatch(user -> user.getUsername().equals(domainObject.getUsername()))) {
                throw new UserDuplicateException();
            }
            long lastId = userList.get(userList.size() - 1).getId();
            domainObject.setId(lastId + 1);
            userList.add(domainObject);
        } else {
            List<User> userList = new LinkedList<>();
            domainObject.setId(1);
            userList.add(domainObject);
            storage = new UserStorage(userList);
        }
        String jsonStr = gson.toJson(storage);
        fileDataStorage.writeToFile(jsonStr, FILENAME);
        return domainObject;
    }

    @Override
    public void remove(User domainObject) {
    }

    @Override
    public User update(User domainObject) {
        return null;
    }

    @Override
    public User findOneById(long id) {
        UserStorage storage = fileDataStorage.readFromFile(FILENAME, UserStorage.class);
        User found = storage.getUsers().stream().filter(user -> user.getId() == id)
                .findFirst().orElse(null);
        if (found != null) return found;
        return new User();
    }
}
