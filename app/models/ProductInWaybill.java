package models;

import javax.persistence.*;

/**
 * Created by Maxim on 2/14/2016.
 */
@Entity
@Table(name = "product_in_wvd")
@IdClass(WaybillVehicleDriver.class)
public class ProductInWaybill {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    public Waybill waybill;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    public Vehicle vehicle;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    public User driver;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            insertable = false,
            updatable = false)
    public Product product;

    @Column(name = "quantity",updatable = true)
    public Integer quantity;

}