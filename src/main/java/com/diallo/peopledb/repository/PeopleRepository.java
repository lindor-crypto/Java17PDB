package com.diallo.peopledb.repository;

import com.diallo.peopledb.annotation.SQL;
import com.diallo.peopledb.interfaces.PeopleDAO;
import com.diallo.peopledb.model.CrudOperation;
import com.diallo.peopledb.model.Region;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PeopleRepository extends CRUDRepository<Person>  implements PeopleDAO
         {
    private AddresRepository addresRepository= null;
    public static final String INSERTER = "Insert into PEOPLE(FIRST_NAME, LAST_NAME,EMAIL, DATE_OF_B) VALUES(?,?,?,?)";
    public static final String ADDUSER = "Insert into PEOPLE(LAST_NAME,FIRST_NAME,EMAIL, PRIMARY_ADDRESS, PARENT_ID) VALUES(?,?,?,?,?)";
    public static final String UPDATE = "Update PEOPLE set  FIRST_NAME=?, LAST_NAME=?,EMAIL=? where ID=?";
    public static final String SELECTOR = "Select p.id,p.first_name, p.last_name,  p.email, p.PRIMARY_ADDRESS,  a.id AS A_ID, a.street_address, a.city, a.county, a.state, a.postcode, a.region from PEOPLE as p left outer join addresses as a on p.PRIMARY_ADDRESS = p.ID where p.email=?";
    public static final String FIND_BY_ID = """
            
            Select 
            p.id,p.first_name, p.last_name,  p.email , p.PRIMARY_ADDRESS,
            a.ID AS A_ID, a.street_address, a.city, a.county, a.state, a.postcode, a.region,
            from PEOPLE AS p 
            left outer join addresses AS a on p.PRIMARY_ADDRESS = a.ID
            where p.ID=?
                 
            """;
    public static final String DELETE = "Delete from  PEOPLE where email=?";
    public static final String DELETE_ONE = "Delete from  PEOPLE where ID=?";
    public static final String DELETE_ALL = "Delete from  PEOPLE where ID > 3";
    public static final String ALLUSERS = "Select   p.id,p.first_name, p.last_name,  p.email , p.PRIMARY_ADDRESS, a.ID AS A_ID, a.street_address, a.city, a.county, a.state, a.postcode, a.region  from PEOPLE AS p left outer join addresses AS a on p.PRIMARY_ADDRESS = a.ID  ";
    private int rowCount;
    private  Map<String, Integer> aliasColIndexMap = new HashMap<>();

    public PeopleRepository(Connection connection ) {
        super(connection);
        addresRepository = new AddresRepository(connection);
        // super(connection);
    }

    /**
     *  Interface Crud repository
     * @param entity
     * @param ps
     * @throws SQLException
     */
    @Override
    @SQL(value = ADDUSER, operationType = CrudOperation.SAVE)
    void mapForSave(Person entity, PreparedStatement ps) throws SQLException {
        Address address = null;

        ps.setString(1, entity.getLastname());
        ps.setString(2, entity.getFirstname());
        ps.setString(3, entity.getEmail());
        associatedAddressWithPerson(ps,entity.getHomeAddress(),4);
        Optional<Person> parent = entity.getParent();
        associatedChildWithParent(ps, parent,5);
        //  associatedAddressWithPerson(ps,entity.getBusinessAddress(),5);
    }

    private static void associatedChildWithParent(PreparedStatement ps, Optional<Person> parent, int parameterIndex) throws SQLException {
        if (parent.isPresent()){
            ps.setLong(parameterIndex, parent.get().getId());
        }else{
            ps.setObject(parameterIndex, null);
        }
    }

    @Override
    protected void postSave(Person entity, Long id) {
        entity.getChildren().stream()
                .forEach(this::save);
    }

    private void associatedAddressWithPerson(PreparedStatement ps, Optional<Address> homeAddress, int parameterIndex) throws SQLException {
        Address address;
        if (homeAddress.isPresent()) {
            address = addresRepository.save(homeAddress.get());
            ps.setLong(parameterIndex, address.id());
        }else {
            ps.setObject(parameterIndex, null);
        }
    }

    @Override
    @SQL(value = SELECTOR, operationType = CrudOperation.FIND_BY_EMAIL)
    @SQL(value = FIND_BY_ID, operationType = CrudOperation.FIND_BY_ID)
    @SQL(value = ALLUSERS, operationType = CrudOperation.FIND_ALL)
    @SQL(value = DELETE, operationType = CrudOperation.DELETE)
    @SQL(value = DELETE_ONE, operationType = CrudOperation.DELETE_ONE)
    @SQL(value = DELETE_ALL, operationType = CrudOperation.DELETE_ALL)
    Person getTFromResulSet(ResultSet rs) throws SQLException {
       // long primary_addressID = rs.getLong("PRIMARY_ADDRESS");
      // Optional<Address> home_address = Optional.ofNullable(addresRepository.findByID(primary_addressID));
      //person.setHomeAddress(home_address.orElse(null));
       // Address address = extractAdress(rs, "HOME_");  si on veut rajouter une autre adresse comme businesse adresse
        Address address = extractAdress(rs);
        Person person = new Person(rs.getLong("ID"), (rs.getString("FIRST_NAME")),
                (rs.getString("LAST_NAME")),
                (rs.getString("EMAIL")));
        person.setHomeAddress(address);

        return person;
    }

    private  Address extractAdress(ResultSet rs) throws SQLException {
        Long idAddress = getValueByAlias("A_ID", rs, Long.class);
        if(idAddress== null) return null;
        String street_address =  rs.getString("street_address");
        String city =  rs.getString("city");
        String county = rs.getString("county");
        String  state = rs.getString("state");
        String postcode = rs.getString("postcode");
        Region region = Region.valueOf(rs.getString("region").toUpperCase());
        Address address = new Address(idAddress,street_address, city, county, state, postcode, region);
        return address;
    }
     private   <T> T getValueByAlias(String alias, ResultSet rs, Class<T> clazz) throws SQLException {
         int columnCount = rs.getMetaData().getColumnCount();
         int foundIndex = getFoundIndex(alias, rs, columnCount);
         //throw new SQLException(String.format("Column not found for alias: '%s'", alias));
         return foundIndex == 0 ? null : (T) rs.getObject(foundIndex);
    }


    private  int getFoundIndex(String alias, ResultSet rs, int columnCount) throws SQLException {
        Integer foundIndex = aliasColIndexMap.getOrDefault(alias, 0);
        if (foundIndex==null) {
            for(int colIdx = 1; colIdx<= columnCount; colIdx++){
                    if(alias.equals( rs.getMetaData().getColumnLabel(colIdx))){
                       //return (T) rs.getObject(colIdx);
                        foundIndex =colIdx;
                        aliasColIndexMap.put(alias, foundIndex);
                        break;
                    }
            }
        }
        return foundIndex;
    }

             @Override
    @SQL(value = SELECTOR, operationType = CrudOperation.FIND_BY_EMAIL)
    void verifyUser(Person p, PreparedStatement ps) throws SQLException {
         ps.setString(1, p.getEmail());
    }
    @Override
    @SQL(value = UPDATE, operationType = CrudOperation.UPDATE)
    protected void mapUpDate(Person p, PreparedStatement ps) throws SQLException {
        ps.setString(1, p.getFirstname());
        ps.setString(2, p.getLastname());
        ps.setString(3, p.getEmail());
    }

    /**
     * quand j'appelle le constructor vide depuis la DAO
     */
    public PeopleRepository() {
        super();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }
    @Override
    public int retunrId(Person person) {
        return 0;
    }
         }
