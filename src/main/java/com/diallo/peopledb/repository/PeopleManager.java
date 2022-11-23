package com.diallo.peopledb.repository;

import com.diallo.peopledb.interfaces.PeopleDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class PeopleManager {
    private PeopleDAO peopleDAO;

 /* public PeopleManager() {
        this.peopleDAO = PeopleDAOFactory.getPeopleDAO();
    }*/
    public PeopleManager(Connection connection) {
        this.peopleDAO = PeopleDAOFactory.getPeopleDAO(connection);
    }

    public Person inserAUSer(Person p) throws SQLException {
        //logic du code

        return peopleDAO.save(p);
    }

    public Person findPuserByEmail(String s) {
        //logic code
        return  peopleDAO.findPuserByEmail(s);
    }
    public void  updateUser(Person p){

    }

    public Set<Person> allUsers() throws SQLException {
        return  peopleDAO.allUsers();
    }
    public  boolean deleteUser(String s) throws SQLException {
        if ( peopleDAO.findPuserByEmail(s)==null){
           // return  peopleDAO.deleteUserByEmail(s);
            return  false;
        }else {
            return   true;
        }
    }
    public int update(Person p) throws SQLException {
        return peopleDAO.update(p);
    }
}
