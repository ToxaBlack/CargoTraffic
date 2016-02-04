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
    @Column(name = "id")
    public long id;

    @Constraints.Required
    @Column(name = "vehicle_producer")
    public String vehicleProducer;

    @Constraints.Required
    @Column(name = "vehicle_model")
    public String vehicleModel;

    @Constraints.Required
    @Column(name = "products_constraint")
    public Double productsConstraintValue;

    @Constraints.Required
    @Column(name = "license_plate")
    public String licensePlate;

    @Constraints.Required
    @Column(name = "fuel_consumption")
    public Double fuelConsumption;

    @ManyToOne
    @JoinColumn(name = "vehicle_fuel_id")
    public VehicleFuel vehicleFuel;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    public VehicleType vehicleType;

    @ManyToOne
    @JoinColumn(name = "company_id")
    public Company company;

    @Transient
    public User driver;

    public Boolean deleted;
}
