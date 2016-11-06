package com.phonebook.dao;

import com.phonebook.entity.User;

public interface UserRepository extends DomainObjectRepository<User> {

    User getUserByUsername(String username);
}
