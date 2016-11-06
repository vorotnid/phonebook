package com.phonebook.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class User extends DomainObject {

    @Pattern(regexp = "[a-zA-Z]+", message = "{username.type}")
    @Size(min = 3, message = "{username.size}")
    private String username;

    @Size(min = 5, message = "{password.size}")
    private String password;

    @Size(min = 4, message = "{userFirstName.size}")
    @NotNull(message = "{field.notNull}")
    private String firstName;

    @Size(min = 4, message = "{userLastName.size}")
    @NotNull(message = "{field.notNull}")
    private String lastName;

    @Size(min = 4, message = "{userMiddleName.size}")
    @NotNull(message = "{field.notNull}")
    private String middleName;

    public User(long id) {
        super(id);
    }

    public User(String username, String password, String firstName, String lastName, String middleName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public User() {
    }

    public User(long id, String username, String password, String firstName, String lastName, String middleName) {
        super(id);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!username.equals(user.username)) return false;
        return password.equals(user.password);

    }

}
