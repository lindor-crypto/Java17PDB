package com.diallo.peopledb.repository;

public class OldMethod {
    /*
    @Override
    public List<Person> allUsers() throws SQLException {
        List<Person> list = new ArrayList<>();
        Person p ;
        PreparedStatement st = getConnection().prepareStatement(allUsers);
        ResultSet result = st.executeQuery();
        while (result.next()){
            p = getT(result);
            list.add(p);
        }
        return list;
    }*/
    /*
    @Override
    public Person findPuserByEmail(String email){
        Person p = null;

        try {
            PreparedStatement ps = getConnection().prepareStatement(selectUser);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()){
                p = getT(rs);
                //p.setDob(rs.getDate(5).toLocalDate());

            }
            rs.close();
            ps.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }
*/
/*
    @Override
    public long verifyExixtingUser(Person p) throws SQLException {

        try {
            PreparedStatement ps = getConnection().prepareStatement(selectUser);
            ps.setString(1, p.getEmail());
            rs = ps.executeQuery();
            while (rs.next()){

                    val = rs.getLong(1);
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(val);
        return val;

    }*/

    /* @Override
     public Person save(Person person) throws SQLException {

           long idUser = verifyExixtingUser(person);
           if (idUser == 0){
               try {

                   PreparedStatement ps= getConnection().prepareStatement(insertPerson2, PreparedStatement.RETURN_GENERATED_KEYS);
                   ps.setString(1, person.getFirstname());
                   ps.setString(2, person.getLastname());
                   ps.setString(3, person.getEmail());
                 //  ps.setDate(4, Date.valueOf(person.getDob()));
                   int recorAffected = ps.executeUpdate();
                   rs = ps.getGeneratedKeys();
                   while (rs.next()){
                       Long id = rs.getLong(1);
                       person.setId(id);
                   }

                   System.out.printf("Records affected: %d%n", recorAffected);

                   }catch (SQLException e)  {
                   e.printStackTrace();
                   }
           }else {
               System.out.println("users alredy exists" );}



               return person;
   }
*/
    /*
    @Override
    public boolean deleteUserByEmail(String email) throws SQLException {
        boolean bfound = false;
        int rowcount ;
        try {
            PreparedStatement ps = getConnection().prepareStatement(deleteUser);
            ps.setString(1, email);

            rowcount = ps.executeUpdate();
            if (rowcount > 0){
                bfound = true;
            }
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bfound;
    }
*/
    /**
     * methode implantée depuis la classe abstracte CrudRepository
     */
  /*  @Override
    public Connection getConnection() throws SQLException {
         return DriverManager.getConnection("jdbc:h2:~/peopletest".replace("~",System.getProperty("user.home")));
    }

    @Override
    protected String getSelectUser() {
        return selectUser;
    }
    @Override
    protected String getFindallUsersSql() {
        return allUsers;
    }*/
            /*
             @Override
             String getSaveSql() {
                 return addUser;
             }

             @Override
    protected String getString() {
        return deleteUser;
    }*/
    /*
    @Override
    protected String getupDate() {
        return upDate;
    }
     */
        /*
    @Override
    protected String getFindByEmailSql() {
        return selectUser;
    }*/
    //LocalDate dob = LocalDate.parse(a[10], DateTimeFormatter.ofPattern("M/d/yyyy"));
    //LocalTime tob = LocalTime.parse(a[2], DateTimeFormatter.ofPattern(""));
    // LocalDateTime dtob = LocalDateTime.of(dob, tob);
    //  ZonedDateTime zdtob = ZonedDateTime.of(dtob, ZoneId.of("+0"));
    //person.setSalary(new BigDecimal(a[25]));
}
