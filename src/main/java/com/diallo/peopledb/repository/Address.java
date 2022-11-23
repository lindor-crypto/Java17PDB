package com.diallo.peopledb.repository;

import com.diallo.peopledb.annotation.Id;
import com.diallo.peopledb.model.Region;

import java.util.Objects;

public final class Address {
    @Id
    private final Long id;
    private final String streetAdress;
    private final String city;
    private final String county;
    private final String state;
    private final String postcode;
    private final Region region;

    public Address(@Id Long id, String streetAdress, String city, String county, String state, String postcode, Region region) {
        this.id = id;
        this.streetAdress = streetAdress;
        this.city = city;
        this.county = county;
        this.state = state;
        this.postcode = postcode;
        this.region = region;
    }


    @Id
    public Long id() {
        return id;
    }

    public String streetAdress() {
        return streetAdress;
    }

    public String city() {
        return city;
    }

    public String county() {
        return county;
    }

    public String state() {
        return state;
    }

    public String postcode() {
        return postcode;
    }

    public Region region() {
        return region;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Address that = (Address) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.streetAdress, that.streetAdress) &&
                Objects.equals(this.city, that.city) &&
                Objects.equals(this.county, that.county) &&
                Objects.equals(this.state, that.state) &&
                Objects.equals(this.postcode, that.postcode) &&
                Objects.equals(this.region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, streetAdress, city, county, state, postcode, region);
    }

    @Override
    public String toString() {
        return "Address[" +
                "id=" + id + ", " +
                "streetAdress=" + streetAdress + ", " +
                "city=" + city + ", " +
                "county=" + county + ", " +
                "state=" + state + ", " +
                "postcode=" + postcode + ", " +
                "region=" + region + ']';
    }


}
