package com.phonebook.dao.jdbc;

import com.phonebook.dao.ContactRepository;
import com.phonebook.entity.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("database")
@Repository
public class JdbcContactRepository implements ContactRepository {

    private final static Logger LOG = LoggerFactory.getLogger(JdbcContactRepository.class);

    private final static String SQL_INSERT = "insert into contact (first_name, last_name, middle_name, cell_phone," +
            "home_phone, address, email, user_id) values (?,?,?,?,?,?,?,?)";
    private final static String SQL_UPDATE = "update contact set first_name=?, last_name=?, middle_name=?," +
            " cell_phone=?, home_phone=?, address=?, email=? where id=?";
    private final static String SQL_REMOVE = "delete from contact where id=?";
    private final static String SQL_SELECT_ALL = "select * from contact where user_id=(select id from user where username=?)";
    private final static String SQL_SELECT_CONTACT = "select * from contact where id=?";

    private JdbcOperations jdbcOperations;

    @Autowired
    public JdbcContactRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Contact add(Contact contact) {
        jdbcOperations.update(SQL_INSERT,
                contact.getFirstName(), contact.getLastName(),
                contact.getMiddleName(), contact.getCellPhoneNumber(),
                contact.getHomePhoneNumber(), contact.getAddress(),
                contact.getEmail(), contact.getUser().getId());
        return null;
    }

    @Override
    public List<Contact> findAllByUsername(String username) {
        return jdbcOperations.query(SQL_SELECT_ALL, (rs, rowNum) -> {
            return new Contact(rs.getLong("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("middle_name"),
                    rs.getString("cell_phone"), rs.getString("home_phone"),
                    rs.getString("address"), rs.getString("email"));
        }, username);
    }

    @Override
    public void remove(Contact contact) {
        jdbcOperations.update(SQL_REMOVE, contact.getId());
    }

    @Override
    public Contact update(Contact contact) {
        jdbcOperations.update(SQL_UPDATE, contact.getFirstName(), contact.getLastName(),
                contact.getMiddleName(), contact.getCellPhoneNumber(),
                contact.getHomePhoneNumber(), contact.getAddress(), contact.getEmail(), contact.getId());
        return findOneById(contact.getId());
    }

    @Override
    public Contact findOneById(long id) {
        return jdbcOperations.queryForObject(SQL_SELECT_CONTACT, (rs, rowNum) -> {
            return new Contact(
                    rs.getLong("id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("middle_name"),
                    rs.getString("cell_phone"), rs.getString("home_phone"),
                    rs.getString("address"), rs.getString("email"));
        }, id);
    }
}
