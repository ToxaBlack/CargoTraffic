package models;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by dmitriy on 5.2.16.
 */

@Entity
@Table(name = "vehicle_type")
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    @Column(name = "name")
    public String vehicleType;
}
