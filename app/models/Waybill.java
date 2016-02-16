package models;

import models.statuses.WaybillStatus;

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
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "departure_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date departureDate;

    @Column(name = "arrival_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date arrivalDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "packing_list_id", nullable = false, referencedColumnName = "id")
    public PackingList packingList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false, referencedColumnName = "id")
    public User manager;

    @Column(name = "status")
    public WaybillStatus status;

    @OneToMany(mappedBy = "waybill")
    public List<WaybillVehicleDriver> vehicleDriverList;

    @OneToMany(mappedBy = "waybill")
    public List<Waypoint> waypointList;
}
