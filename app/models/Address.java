package models;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by Anton Chernov on 1/18/2016.
 */
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Constraints.Required
    public long id;

    public String country;

    public String city;

    public String street;

    public String house;

    public String flat;

    public Address(String country, String city, String street, String house, String flat) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
        this.flat = flat;
    }

    public Address() {
    }
}
