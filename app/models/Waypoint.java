package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.statuses.WaypointStatus;

import javax.persistence.*;

/**
 * Created by Olga on 13.02.2016.
 */
@Entity
@Table(name = "waypoint")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Waypoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Float lat;

    public Float lng;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public WaypointStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_id")
    public Waybill waybill;

    public Waypoint() {
    }

}
