package models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Maxim on 2/14/2016.
 */

@Embeddable
@Table(name = "waybill_vehicle_driver")
public class WaybillVehicleDriver implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    public Waybill waybill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    public Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    public User driver;

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (waybill != null ? waybill.hashCode() : 0);
        result = 31 * result + (vehicle != null ? vehicle.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        WaybillVehicleDriver that = (WaybillVehicleDriver) obj;

        return (id != null ? id.equals(that.id) : that.id == null)
                && (waybill != null ? waybill.equals(that.waybill) : that.waybill == null)
                && (vehicle != null ? vehicle.equals(that.vehicle) : that.vehicle == null)
                && (driver != null ? driver.equals(that.driver) : that.driver == null);
    }
}
