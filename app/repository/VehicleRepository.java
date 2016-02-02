package repository;

import models.Company;
import models.Vehicle;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by dmitriy on 3.2.16.
 */

public class VehicleRepository {
    private static final Logger.ALogger LOGGER = Logger.of(VehicleRepository.class);


    public List<Vehicle> getVehicles(long id, int count, boolean ascOrder, long companyId) {
        LOGGER.debug("Get page: {}, {}, {}", id, count, ascOrder);
        EntityManager em = JPA.em();

        return null;
    }

    public Vehicle updateVehicle(Vehicle vehicle, long companyId) {
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
}
