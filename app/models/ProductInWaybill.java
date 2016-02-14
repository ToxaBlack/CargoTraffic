package models;

import javax.persistence.*;

/**
 * Created by Maxim on 2/14/2016.
 */
@Entity
@Table(name = "product_in_wvd")
public class ProductInWaybill {
    @EmbeddedId
    @JoinColumn(
            name = "wvd_id",
            insertable = false,
            updatable = false)
    public WaybillVehicleDriver waybillVehicleDriver;

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
