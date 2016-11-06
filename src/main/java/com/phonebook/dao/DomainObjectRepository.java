package com.phonebook.dao;

import com.phonebook.exception.PhoneNumberDuplicateException;
import com.phonebook.exception.UserDuplicateException;

public interface DomainObjectRepository<T> {
    T add(T domainObject) throws PhoneNumberDuplicateException, UserDuplicateException;

    void remove(T domainObject);

    T update(T domainObject);

    T findOneById(long id);
}
