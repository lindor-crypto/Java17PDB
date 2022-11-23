package com.diallo.peopledb.repository;

import com.diallo.peopledb.model.Region;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeopleRepositoryTests {

    public   Connection connection;
    private PeopleManager repo;
    private  PeopleRepository peopleRepository;



    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:~/peopletest;TRACE_LEVEL_SYSTEM_OUT=0".replace("~",System.getProperty("user.home")));
        repo = new PeopleManager(connection);
        //repo = new PeopleManager(); //si j'utilise le constructor PeopleRepository();
        peopleRepository = new PeopleRepository(connection);
       // connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection!=null){
            connection.close();
        }
    }

    @Test
    @Disabled
    public void loadData() throws IOException, SQLException {
        Files.lines(Path.of("C:/Users/LDO/Downloads/DOC ASS/us-500/us-500.csv"))
                .skip(1)
                .limit(100)
                .map(l -> l.split(";"))
                .map(a -> {
                    Person person = new Person(a[0], a[1], a[10]);
                    return person;
                })
                .forEach(peopleRepository::save);
        connection.commit();
    }

    @Test
    void recordPerson() throws SQLException {


        Person john = new Person("D123", "Zigi","ldiallo@zigi.fr");
       // Person savePerson = repo.inserAUSer(john);
        Person savePerson = peopleRepository.save(john);
        assertThat(savePerson.getId()).isGreaterThan(0);

    }
    @Test
    void recordTwoPerson() throws SQLException {

        Person john = new Person("Fall","Diallo","");
        Person boby = new Person("baby","seck","");

        Person savePerson = repo.inserAUSer(john);
        Person saveBoby = repo.inserAUSer(boby);
        assertThat(savePerson.getId()).isGreaterThan(0);
        assertThat(saveBoby.getId()).isGreaterThan(0);
        assertThat(savePerson.getId()).isNotEqualTo(saveBoby.getId());

    }
    @Test
    public void canSavePersonWithAddress() throws SQLException {
        Person john = new Person("leuzkinepiZZZ", "pikine","leuzkinepio@zigi.fr");
        Address adress = new Address(null,"6649 N Blue Gum St", "New Orleans", "Orleans", "LA", "70116", Region.WEST);
        john.setHomeAddress(adress);
       Person p=  peopleRepository.save(john);
        assertThat(p.getHomeAddress().get().id()).isGreaterThan(0);
    }

    @Test
    public void canFindUserByEmail() throws SQLException {
       // Person boby = new Person("test","test","db@db.fr", LocalDate.of(2007,11,23));
       // System.out.println(peopleRepository.findPuserByEmail("abc@abc.fr"));
        System.out.println(peopleRepository.findPuserByEmail("ldo@gmail.com"));

       // Person savePerson = repo.save(boby);
        //Person foundPerson = repo.findPuserByEmail(savePerson.getEmail());
        //assertThat(foundPerson).isEqualTo(savePerson);

    }
    @Test
    @Disabled
    public void canFindPersonByWithAddress() throws SQLException {
        Person john = new Person("leuzkinepiZZZ", "pikine","leuzkinepio@zigi.fr");
        Address adress = new Address(null,"6649 N Blue Gum St", "New Orleans", "Orleans", "LA", "70116", Region.WEST);
        john.addChild(new Person("fils1", "fila1","fils1@fila.fr"));
        john.setHomeAddress(adress);
        Person p=  peopleRepository.save(john);
        Person pers = peopleRepository.findByID(p.getId());
        assertThat(pers.getHomeAddress().get().state()).isEqualTo("LA");
    }
    @Test
    public  void  canSavePersonWithCildren() throws SQLException {
        Person john = new Person("John", "pikine","leuzkinepio@zigi.fr");
        john.addChild(new Person("fils1", "fila1","fils1@fila.fr"));
        john.addChild(new Person("fils2", "fila2","fils2@fila.fr"));
        john.addChild(new Person("fils3", "fila3","fils3@fila.fr"));
        Person saveperson = peopleRepository.save(john);

        saveperson.getChildren().stream()
                .map(Person::getId)
                .forEach(id -> assertThat(id).isGreaterThan(0));
        connection.commit();

    }
    @Test
    @Disabled
    public  void  canFindPersonByIdWithCildren(){
        Person john = new Person("John", "pikine","leuzkinepio@zigi.fr");
        john.addChild(new Person("fils1", "fila1","fils1@fila.fr"));
        john.addChild(new Person("fils2", "fila2","fils2@fila.fr"));
        john.addChild(new Person("fils3", "fila3","fils3@fila.fr"));
        Person saveperson = peopleRepository.save(john);
        Person foundPerson = peopleRepository.findByID(saveperson.getId());
        assertThat(foundPerson.getChildren().stream().map(Person::getFirstname).collect(Collectors.toSet()))
                .contains("fils1", "fils2", "fils3");


    }


    @Test
    @Disabled
    public void canFindPersonByWithBizAddress() throws SQLException {
        Person john = new Person("leuzkinepiZZZ", "pikine","leuzkinepio@zigi.fr");
        Address adress = new Address(null,"6649 N Blue Gum St", "New Orleans", "Orleans", "LA", "70116", Region.WEST);
        john.setBusinessAddress(adress);
        Person p=  peopleRepository.save(john);
        Person pers = peopleRepository.findByID(p.getId());
        assertThat(pers.getBusinessAddress().get().state()).isEqualTo("LA");
    }

    @Test
    public void allUser() throws SQLException {
       // peopleRepository.deleteAll();
        Set<Person> list = peopleRepository.allUsers();
        for (Person p: list) {
            System.out.println(p);

        }
    }
    @Test
    public void canDelete() throws SQLException {
       // Person boby = repo.inserAUSer(new Person("mamy","test","mamy@db.fr"));
        Person boby = peopleRepository.save(new Person("mamy","test","mamy@db.fr"));

       // boolean bfound = repo.deleteUser(boby.getEmail());
         boolean bfound = peopleRepository.deleteUserByEmail(boby.getEmail());
        peopleRepository.delete(boby);
      assertEquals(true,bfound );
    }
    @Test
    @Disabled
    void update() throws SQLException{
        Person boby  = peopleRepository.findPuserByEmail("ilene.eroman@hotmail.com");
            boby.setLastname("Astou");
            boby.setFirstname("diop");
            boby.setEmail("ldo@gmail.com");
            int row =peopleRepository.update(boby);
        assertEquals(1, row);

    }

}
