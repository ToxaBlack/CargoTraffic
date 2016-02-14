package models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Maxim on 2/14/2016.
 */

@Entity
@Table(name = "waybill_vehicle_driver")
public class WaybillVehicleDriver implements Serializable {

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

    @Override
    public int hashCode() {
        int result = 31 * (waybill != null ? waybill.hashCode() : 0);
        result = 31 * result + (vehicle != null ? vehicle.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        WaybillVehicleDriver that = (WaybillVehicleDriver) obj;

        return (waybill != null ? waybill.equals(that.waybill) : that.waybill == null)
                && (vehicle != null ? vehicle.equals(that.vehicle) : that.vehicle == null)
                && (driver != null ? driver.equals(that.driver) : that.driver == null);
    }
}
