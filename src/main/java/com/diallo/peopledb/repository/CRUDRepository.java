package com.diallo.peopledb.repository;

import com.diallo.peopledb.annotation.Id;
import com.diallo.peopledb.annotation.MultiSQL;
import com.diallo.peopledb.annotation.SQL;
import com.diallo.peopledb.dataException.DataException;
import com.diallo.peopledb.model.CrudOperation;

import java.sql.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

//abstract class CRUDRepository<T extends  Entities> {
abstract class CRUDRepository<T> {

    protected Connection connection;
     private long val;
     private       int rowCount;
    private PreparedStatement savePS;
    private PreparedStatement updatePS;

    public CRUDRepository(Connection connection) {

        try {
            this.connection = connection;
            savePS = connection.prepareStatement(getSqlByAnnotation(CrudOperation.SAVE,this::getSaveSql), PreparedStatement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
     public CRUDRepository() {
     }

    /**
     * this.getClass().getDeclaredMethods() référence =>  par exemple peoplerepository
     * filter(m-> "methodName".contentEquals(m.getName())) => filtre par la methode mapforsave contenu das cette classe
     * map(m -> m.getAnnotation(SQL.class)) => qui contient une annotation sql
     * map(SQL::value) => et dont cette sql annotation a une value
     * Supplier<String> sqlGetter => fait référence à la methode qui appelle le String dans le if
     * @return
     */
   /* private String getSqlByAnnotation(String methodName, Supplier<String> sqlGetter){
       return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(m-> methodName.contentEquals(m.getName()))
                .map(m -> m.getAnnotation(SQL.class))
                .map(SQL::value)
                //.findFirst().orElse(getSaveSql());
                .findFirst().orElseGet(sqlGetter);

    }*/
    private String getSqlByAnnotation(CrudOperation operation, Supplier<String> sqlGetter){
        Stream<SQL> multisqlStream = Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(MultiSQL.class))
                .map(m -> m.getAnnotation(MultiSQL.class))
                .flatMap(msql -> Arrays.stream(msql.value()));
        Stream<SQL> sqlStream = Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(SQL.class))
                .map(m -> m.getAnnotation(SQL.class));
        return Stream.concat(multisqlStream,sqlStream)
                .filter(a->a.operationType().equals(operation))
                .map(SQL::value)
                .findFirst().orElseGet(sqlGetter);

    }

     public T save(T entity)  {
            try {
               // PreparedStatement ps= connection.prepareStatement(getSqlByAnnotation("extracted",this::getSaveSql), PreparedStatement.RETURN_GENERATED_KEYS);
                mapForSave(entity, savePS);
                //  ps.setDate(4, Date.valueOf(entity.getDob()));
                int recorAffected = savePS.executeUpdate();
                ResultSet rs = savePS.getGeneratedKeys();
                while (rs.next()){
                    Long id = rs.getLong(1);
                    setIdByAnntotation(id, entity);
                    postSave(entity,id);
                }
                System.out.printf("Records affected: %d%n", recorAffected);

            }catch (SQLException e)  {
                e.printStackTrace();
            }
        return entity;

    }



    public int update(T p) throws SQLException {
        try {
            updatePS = connection.prepareStatement(getSqlByAnnotation(CrudOperation.UPDATE, this::getupDate));
            mapUpDate(p, updatePS);
            updatePS.setLong(4, getIdByAnnotation(p));
            rowCount = updatePS.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw  new DataException("Unable to create prepared statement for crud repository", e);
        }
        return rowCount;

    }
    public  void delete(T entity) throws SQLException {
        try {
            PreparedStatement ps =connection.prepareStatement(getSqlByAnnotation(CrudOperation.DELETE_ONE, this::getDelete));
           // ps.setLong(1, entity.getId());
            ps.setLong(1, getIdByAnnotation(entity));
            int affectedRecordCount = ps.executeUpdate();
            System.out.println(affectedRecordCount);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public  void deleteAll() throws SQLException {
        try {
            Statement ps =connection.createStatement();
            // ps.setLong(1, entity.getId());
           ps.execute(getSqlByAnnotation(CrudOperation.DELETE_ALL, this::getDelete));
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    /**
     * f -> field of class
     * @param entity
     * @return
     */

    private Long getIdByAnnotation(T entity){
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f->f.isAnnotationPresent(Id.class))
                .map(f -> {
                    f.setAccessible(true);
                    Long id= null;
                    try {
                         id = (Long) f.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    return id;
                })
                .findFirst().orElseThrow(()-> new RuntimeException("no Id annotabled field found "));
    }
    private void setIdByAnntotation(Long id, T e){
        Arrays.stream(e.getClass().getDeclaredFields())
                .filter(f->f.isAnnotationPresent(Id.class))
                .forEach(f->{
                    f.setAccessible(true);
                    try {
                        f.set(e,id);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("enable to set ID field Value ");
                    }
                });

    }

    protected void postSave(T entity, Long id) {  }
    public T findPuserByEmail(String email){
         T p = null;
         try {
             PreparedStatement ps = connection.prepareStatement(getSqlByAnnotation(CrudOperation.FIND_BY_EMAIL, this::getFindByEmailSql));
             ps.setString(1, email);
             ResultSet rs = ps.executeQuery();
             while (rs.next()){
                 p = getTFromResulSet(rs);
                 //p.setDob(rs.getDate(5).toLocalDate());
             }
             rs.close();
             ps.close();
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
         return p;
     }
    public T findByID(Long id){
        T p = null;
        try {
            PreparedStatement ps = connection.prepareStatement(getSqlByAnnotation(CrudOperation.FIND_BY_ID, this::getFindByEmailSql));
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                p = getTFromResulSet(rs);
                //p.setDob(rs.getDate(5).toLocalDate());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }
     public long verifyExixtingUser(T p) throws SQLException {

         try {
             PreparedStatement ps = connection.prepareStatement(getSqlByAnnotation(CrudOperation.FIND_BY_EMAIL, this::getSelectUser));
             verifyUser(p, ps);
             ResultSet rs = ps.executeQuery();
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

     }

    public Set<T> allUsers() throws SQLException {
        Set<T> list = new HashSet<>();
        T p ;
        PreparedStatement st = connection.prepareStatement(getSqlByAnnotation(CrudOperation.FIND_ALL, this::getFindallUsersSql));
        ResultSet result = st.executeQuery();
        while (result.next()){
            p = getTFromResulSet(result);
            list.add(p);
        }
        return list;
    }

    public boolean deleteUserByEmail(String email) throws SQLException {
        boolean bfound = false;
        int rowcount ;
        try {
            PreparedStatement ps = connection.prepareStatement(getSqlByAnnotation(CrudOperation.DELETE, this::getString));
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


    protected abstract  void mapUpDate(T p, PreparedStatement ps) throws SQLException ;

    abstract T getTFromResulSet(ResultSet rs) throws SQLException ;
    abstract  void mapForSave(T entity, PreparedStatement ps) throws SQLException;
    abstract void verifyUser(T p, PreparedStatement ps) throws SQLException ;
   protected String getSelectUser(){  throw new RuntimeException("SQL not Specify");   }
    protected String getString(){ throw new RuntimeException("SQL not Specify");     }
    protected String getFindallUsersSql(){ throw new RuntimeException("SQL not Specify");     }
    protected  String getSaveSql(){ throw new RuntimeException("SQL not Specify");     }
    protected String getupDate(){ throw new RuntimeException("SQL not Specify");    }
    protected String getFindByEmailSql(){ throw new RuntimeException("SQL not Specify");    }
    private String getDelete() {throw new RuntimeException("SQL not Specify");   }
}
