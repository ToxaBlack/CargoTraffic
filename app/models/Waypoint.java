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

    public Waypoint() {
    }
}
