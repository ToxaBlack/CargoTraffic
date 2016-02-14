package models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Maxim on 2/14/2016.
 */
@Entity
@Table(name = "product_in_wvd")
public class ProductInWaybill implements Serializable{

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false)
    public Product product;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wvd_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false)
    public WaybillVehicleDriver waybillVehicleDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false)
    public Waybill waybill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false)
    public Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false)
    public User driver;

    @Column(name = "quantity", updatable = true)
    public Long quantity;

    @Override
    public int hashCode() {
        int result = 31 * (product != null ? product.hashCode() : 0);
        result = 31 * result + (waybillVehicleDriver != null ? waybillVehicleDriver.hashCode() : 0);
        result = 31 * result + (waybill != null ? waybill.hashCode() : 0);
        result = 31 * result + (vehicle != null ? vehicle.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ProductInWaybill that = (ProductInWaybill) obj;

        return (product != null ? product.equals(that.product) : that.product == null)
                && (waybillVehicleDriver != null ? waybillVehicleDriver.equals(that.waybillVehicleDriver) : that.waybillVehicleDriver == null)
                && (waybill != null ? waybill.equals(that.waybill) : that.waybill == null)
                && (vehicle != null ? vehicle.equals(that.vehicle) : that.vehicle == null)
                && (driver != null ? driver.equals(that.driver) : that.driver == null)
                && (quantity != null ? quantity.equals(that.quantity) : that.quantity == null);
    }
}