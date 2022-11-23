package com.diallo.peopledb.dataException;

import java.sql.SQLException;

public class DataException extends  RuntimeException{

    public DataException(String s, SQLException e) {
        super(s, e);
    }
    public DataException(String s) {
        super(s);
    }
}
