package com.diallo.peopledb.repository;

import com.diallo.peopledb.annotation.SQL;
import com.diallo.peopledb.model.CrudOperation;
import com.diallo.peopledb.model.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddresRepository extends CRUDRepository<Address>{
    public static final String  SAVEADRE= "Insert into addresses (street_address, city, county, state, postcode, region) values (?,?,?,?,?,?)";
    public static final String  FIND_BY_ID_SQL= "Select  id, street_address, city, county, state, postcode, region from addresses where id=?";


    public AddresRepository(Connection connection) {
        super(connection);
    }

    @Override
    protected void mapUpDate(Address p, PreparedStatement ps) throws SQLException {

    }

    @Override
    @SQL(value = FIND_BY_ID_SQL, operationType = CrudOperation.FIND_BY_ID)
    Address getTFromResulSet(ResultSet rs) throws SQLException {
        long idAddress =  rs.getLong("id");
        String street_address =  rs.getString("street_address");
        String city =  rs.getString("city");
        String county = rs.getString("county");
        String  state =rs.getString("state");
        String postcode = rs.getString("postcode");
        Region region = Region.valueOf( rs.getString("region").toUpperCase());
        Address address = new Address(idAddress, street_address, city, county, state, postcode, region);
        return address;
    }

    @Override
    @SQL(operationType = CrudOperation.SAVE, value = SAVEADRE)
    void mapForSave(Address entity, PreparedStatement ps) throws SQLException {
        ps.setString(1, entity.streetAdress());
        ps.setString(2, entity.city());
        ps.setString(3, entity.county());
        ps.setString(4, entity.state());
        ps.setString(5, entity.postcode());
        ps.setString(6, entity.region().toString());


    }

    @Override
    void verifyUser(Address p, PreparedStatement ps) throws SQLException {

    }
}
