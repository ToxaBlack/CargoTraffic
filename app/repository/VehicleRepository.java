package repository;

import models.Vehicle;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dmitriy on 3.2.16.
 */

public class VehicleRepository {
    private static final Logger.ALogger LOGGER = Logger.of(VehicleRepository.class);


    public List<Vehicle> getVehicles(long id, int count, boolean ascOrder, long companyId) {
        LOGGER.debug("Get page: {}, {}, {}", id, count, ascOrder);
        EntityManager em = JPA.em();

        Vehicle vehicle = new Vehicle();
        vehicle.id = 1;
        vehicle.deleted = false;
        vehicle.fuelConsumption = 15.3;
        vehicle.fuelCost = 12000.0;
        vehicle.fuelName = "Diesel";
        vehicle.licensePlate = "1234-AB";
        vehicle.productsConstraintValue = 1200.5;
        vehicle.vehicleModel = "A123";
        vehicle.vehicleProducer = "Man";
        vehicle.vehicleType = "Refrigerator";
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(vehicle);
        return vehicleList;
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
