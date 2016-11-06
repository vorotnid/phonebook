package com.phonebook.entity;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Contact extends DomainObject {

    @Size(min = 4, message = "{contactFirstName.size}")
    @NotNull(message = "{field.notNull}")
    private String firstName;

    @Size(min = 4, message = "{contactLastName.size}")
    @NotNull(message = "field.notNull")
    private String lastName;

    @Size(min = 4, message = "{contactMiddleName.size}")
    @NotNull(message = "{field.notNull}")
    private String middleName;

    @Pattern(regexp = "\\+380\\([1-9]{2}\\)[0-9]{7}", message = "{cellNumber.valid}")
    @NotNull(message = "{field.notNull}")
    private String cellPhoneNumber;

    private String homePhoneNumber;

    private String address;

    @Email(regexp = ".+@.+\\..+|^$", message = "{email.valid}")
    private String email;

    private User user;

    public Contact(long id) {
        super(id);
    }

    public Contact() {
    }

    public Contact(String firstName, String lastName, String middleName, String cellPhoneNumber, String homePhoneNumber, String address, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.cellPhoneNumber = cellPhoneNumber;
        this.homePhoneNumber = homePhoneNumber;
        this.address = address;
        this.email = email;
    }

    public Contact(long id, String firstName, String lastName, String middleName, String cellPhoneNumber, String homePhoneNumber, String address, String email) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.cellPhoneNumber = cellPhoneNumber;
        this.homePhoneNumber = homePhoneNumber;
        this.address = address;
        this.email = email;
    }

    public Contact(long id, String firstName, String lastName, String middleName, String cellPhoneNumber,
                   String homePhoneNumber, String address, String email, User user) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.cellPhoneNumber = cellPhoneNumber;
        this.homePhoneNumber = homePhoneNumber;
        this.address = address;
        this.email = email;
        this.user = user;
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

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (!firstName.equals(contact.firstName)) return false;
        if (!lastName.equals(contact.lastName)) return false;
        if (!middleName.equals(contact.middleName)) return false;
        if (!cellPhoneNumber.equals(contact.cellPhoneNumber)) return false;
        if (homePhoneNumber != null ? !homePhoneNumber.equals(contact.homePhoneNumber) : contact.homePhoneNumber != null)
            return false;
        if (address != null ? !address.equals(contact.address) : contact.address != null) return false;
        return email != null ? email.equals(contact.email) : contact.email == null;

    }
}
