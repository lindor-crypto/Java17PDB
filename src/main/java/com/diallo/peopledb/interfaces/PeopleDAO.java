package com.diallo.peopledb.interfaces;

import com.diallo.peopledb.repository.Person;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface PeopleDAO {
    // }
    Connection getConnection() throws SQLException;

    Set<Person> allUsers() throws SQLException;
    Person findPuserByEmail(String email);
    long verifyExixtingUser(Person p) throws SQLException;
    Person save(Person person) throws SQLException;

    int retunrId(Person person);

    boolean deleteUserByEmail(String email)throws SQLException;
    int update(Person p) throws SQLException;
}
