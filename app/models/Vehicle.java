package models;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by dmitriy on 1.2.16.
 */

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Constraints.Required
    public String vehicleProducer;

    @Constraints.Required
    public String vehicleModel;

    @Constraints.Required
    public String licensePlate;

    @Constraints.Required
    public Double fuelConsumption;

    @Constraints.Required
    public Double fuelCost;

    @Constraints.Required
    public String vehicleType;

    @ManyToOne
    public User driver;

    @Constraints.Required
    public Boolean deleted;
}