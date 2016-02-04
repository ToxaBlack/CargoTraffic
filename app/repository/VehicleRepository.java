package repository;

import models.Company;
import models.Vehicle;
import models.VehicleFuel;
import models.VehicleType;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by dmitriy on 3.2.16.
 */

public class VehicleRepository {
    private static final Logger.ALogger LOGGER = Logger.of(VehicleRepository.class);


    public List<Vehicle> getVehicles(long id, int count, boolean ascOrder, long companyId) {
        LOGGER.debug("Get vehicles: {}, {}, {}", id, count, ascOrder);
        EntityManager em = JPA.em();

        StringBuilder stringBuilder = new StringBuilder("SELECT v FROM Vehicle v LEFT JOIN FETCH v.vehicleFuel " +
                "LEFT JOIN FETCH v.vehicleType LEFT JOIN FETCH v.company WHERE ");
        if (ascOrder) {
            stringBuilder.append("v.id >= ? AND v.company.id = ? ORDER BY v.id ASC");
        } else {
            stringBuilder.append("v.id < ?  AND v.company.id = ? ORDER BY v.id DESC");
        }
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter(1, id);
        query.setParameter(2, companyId);
        query.setMaxResults(count);
        List<Vehicle> vehicles = query.getResultList();
        if (!ascOrder)
            Collections.reverse(vehicles);
        return vehicles;
    }

    public Vehicle updateVehicle(Vehicle vehicle) {
        LOGGER.debug("Update vehicle: {}", vehicle.licensePlate);
        EntityManager em = JPA.em();
        return null;
    }

    public Vehicle addVehicle(Vehicle vehicle, long companyId) {
        LOGGER.debug("Adding vehicle: {}", vehicle.licensePlate);
        EntityManager em = JPA.em();
        em.persist(vehicle);
        em.refresh(vehicle);
        return vehicle;
    }

    public Vehicle deleteVehicles(List<Long> vehicleIds) {
        LOGGER.debug("Delete vehicles: {}", Arrays.toString(vehicleIds.toArray()));
        EntityManager em = JPA.em();
        return null;
    }
}
