package models;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by dmitriy on 5.2.16.
 */

@Entity
@Table(name = "vehicle_fuel")
public class VehicleFuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    @Column(name = "name")
    public String fuelName;

    @Constraints.Required
    @Column(name = "cost")
    public Double fuelCost;
}
