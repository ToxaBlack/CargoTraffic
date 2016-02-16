package models;

import models.statuses.WaypointStatus;

import javax.persistence.*;

/**
 * Created by Olga on 13.02.2016.
 */
@Entity
@Table(name = "waypoint")
public class Waypoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public long id;

    public float lat;

    public float lng;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public WaypointStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_id", nullable = false, referencedColumnName = "id")
    public Waybill waybill;

    public Waypoint() {
    }

}
