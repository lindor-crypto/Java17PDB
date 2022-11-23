package com.diallo.peopledb.repository;


import com.diallo.peopledb.annotation.Id;

import java.time.LocalDate;
import java.util.*;

public class Person  {
    @Id
    private Long id;

    private String lastname;
    private String firstname;
    private LocalDate dob;
    private String email;
    private Optional<Address> homeAddress = Optional.empty();
    private Optional<Address> businessAddress= Optional.empty();
    private Set<Person> children = new HashSet<>();
    private Optional<Person> parent = Optional.empty();


    public Optional<Address> getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = Optional.ofNullable(homeAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id) && lastname.equals(person.lastname) && firstname.equals(person.firstname) && email.equals(person.email);
    }

    @Override
    public int hashCode() {   return Objects.hash(id, lastname, firstname, email);  }

    public Person(String lastname, String firstname , String email, LocalDate dob) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.dob = dob;
        this.email = email;
    }

    public Person() {
    }

    public Person(String lastname, String firstname , String email) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
    }

    public Person(Long id, String lastname, String firstname , String email) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
    }


    public Long getId() {
        //return 1L;
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder  = new StringBuilder();
        stringBuilder
                .append("les infos de la personne  sont : ")
                .append("ID: " +getId())
                .append(" ")
                .append(getFirstname()).append(" ").append(getLastname()).append(" ").append(getEmail())
                .append(" ")
                .append(getHomeAddress())

        ;
        return stringBuilder.toString();

    }

    public void setBusinessAddress(Address businessAddress) {
        this.businessAddress = Optional.ofNullable(businessAddress);
    }

    public Optional<Address> getBusinessAddress() {
        return businessAddress;
    }

    public void addChild(Person child) {
        children.add(child);
        child.setParent(this);

    }

    public void setParent(Person parent) {
        this.parent = Optional.ofNullable(parent);
    }

    public Optional<Person> getParent() {
        return parent;
    }

    public Set<Person> getChildren() {
        return  children;
    }
}
