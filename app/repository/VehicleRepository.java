package repository;

import models.Vehicle;
import models.VehicleFuel;
import models.VehicleType;
import org.apache.commons.collections4.CollectionUtils;
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
        LOGGER.debug("Get vehicles: {}, {}, {}", id, count, ascOrder);
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT v FROM Vehicle v LEFT JOIN FETCH v.vehicleFuel " +
                "LEFT JOIN FETCH v.vehicleType LEFT JOIN FETCH v.company WHERE ");
        if (ascOrder) {
            stringBuilder.append("v.id >= ? AND v.company.id = ? AND v.deleted = false ORDER BY v.id ASC");
        } else {
            stringBuilder.append("v.id < ?  AND v.company.id = ? AND v.deleted = false ORDER BY v.id DESC");
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
        em.merge(vehicle);
        return vehicle;
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        LOGGER.debug("Adding vehicle: {}", vehicle.licensePlate);
        EntityManager em = JPA.em();
        em.persist(vehicle);
        em.refresh(vehicle);
        return vehicle;
    }

    public void deleteVehicles(List<Long> vehicleIds) {
        LOGGER.debug("Delete vehicles: {}", Arrays.toString(vehicleIds.toArray()));
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("UPDATE Vehicle v SET v.deleted = true WHERE v.id in (");
        for (Long id : vehicleIds) {
            stringBuilder.append(id);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        Query query = em.createQuery(stringBuilder.toString());
        query.executeUpdate();
    }

    public VehicleType getVehicleTypeByName(String typeName) {
        LOGGER.debug("Get vehicle type: {}", typeName);
        EntityManager em = JPA.em();
        String selectSql = new String("SELECT t FROM VehicleType t WHERE t.vehicleType = ?");
        Query query = em.createQuery(selectSql);
        query.setParameter(1, typeName);
        List<VehicleType> vehicleTypeList = query.getResultList();
        if (CollectionUtils.isNotEmpty(vehicleTypeList))
            return vehicleTypeList.get(0);
        return null;
    }

    public VehicleFuel getVehicleFuelByName(String fuelName) {
        LOGGER.debug("Get vehicle fuel: {}", fuelName);
        EntityManager em = JPA.em();
        String selectSql = new String("SELECT f FROM VehicleFuel f WHERE f.fuelName = ?");
        Query query = em.createQuery(selectSql);
        query.setParameter(1, fuelName);
        List<VehicleFuel> vehicleFuelList = query.getResultList();
        if (CollectionUtils.isNotEmpty(vehicleFuelList))
            return vehicleFuelList.get(0);
        return null;
    }
}
