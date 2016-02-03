package models;

import models.enums.WaybillStatus;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dmitriy on 1.2.16.
 */

@Entity
@Table(name = "waybill")
public class Waybill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Constraints.Required
    public Date departureDate;

    public Date arrivalDate;

    @ManyToOne
    public Warehouse departureWarehouse;

    @ManyToOne
    public Warehouse destinationWarehouse;

    @OneToOne
    public PackingList packingList;

    @OneToMany
    public List<Vehicle> vehicleList;

    @ManyToOne
    public User manager;

    public WaybillStatus status;
}