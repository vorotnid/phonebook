package com.phonebook.dao.jdbc;

import com.phonebook.entity.User;
import com.phonebook.dao.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Profile("database")
@Repository
public class JdbcUserRepository implements UserRepository {

    private final static Logger LOG = LoggerFactory.getLogger(JdbcUserRepository.class);
    private final static String SQL_INSERT = "insert into user (username, first_name, last_name," +
            " middle_name, password) values (?,?,?,?,?)";
    private static final String SQL_SELECT_USER = "select * from user where id =?";
    private JdbcOperations jdbcOperations;

    @Autowired
    public JdbcUserRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public User add(User user) {
        jdbcOperations.update(SQL_INSERT, user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getMiddleName(), user.getPassword());
        return getUserByUsername(user.getUsername());
    }

    @Override
    public void remove(User user) {

    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User findOneById(long id) {
        return jdbcOperations.queryForObject(SQL_SELECT_USER, (rs, rowNum) -> {
            return new User(
                    rs.getLong("id"), rs.getString("username"),
                    rs.getString("password"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("middle_name"));
        }, id);
    }

    public User getUserByUsername(String username) {
        return jdbcOperations.queryForObject("select * from user where username=?",
                (rs, rowNum) -> {
                    return new User(rs.getLong("id"), rs.getString("username"),
                            rs.getString("password"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("middle_name"));
                }, username);
    }
}
