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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Constraints.Required
    public long id;

    public String country;

    public String city;

    public String street;

    public String house;

    public String flat;
}
