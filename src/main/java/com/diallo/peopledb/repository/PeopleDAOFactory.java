package com.diallo.peopledb.repository;

import com.diallo.peopledb.interfaces.PeopleDAO;

import java.sql.Connection;

public class PeopleDAOFactory {

    public static PeopleDAO getPeopleDAO(Connection connection){
        return new PeopleRepository(connection);
    }
    /*public static PeopleDAO getPeopleDAO( ){
        return new PeopleRepository();
    }*/
}
