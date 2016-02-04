package service;

import models.User;
import models.Vehicle;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.VehicleRepository;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dmitriy on 3.2.16.
 */

public class VehicleService {
    private static final Logger.ALogger LOGGER = Logger.of(VehicleService.class);

    @Inject
    VehicleRepository vehicleRepository;

    public List<Vehicle> getVehicles(Long id, Integer count, Boolean ascOrder) throws ServiceException {
        LOGGER.debug("id, count, ascOrder: {}, {}, {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                return vehicleRepository.getVehicles(id, count, ascOrder, user.company.id);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get vehicle list error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public Vehicle addVehicle(Vehicle vehicle) throws ServiceException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API add vehicle: {}, {}", oldUser.toString(), vehicle.licensePlate);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                return vehicleRepository.addVehicle(vehicle, user.company.id);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Adding vehicle error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }


    public Vehicle updateVehicle(Vehicle vehicle) throws ServiceException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API update vehicle: {}, {}", oldUser.toString(), vehicle.licensePlate);
        try {
            return JPA.withTransaction(() -> vehicleRepository.updateVehicle(vehicle));
        } catch (Throwable throwable) {
            LOGGER.error("Update vehicle error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public Vehicle deleteVehicles(List<Long> vehicleIds) throws ServiceException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API delete vehicles: {}, {}", oldUser.toString(), Arrays.toString(vehicleIds.toArray()));
        try {
            return JPA.withTransaction(() -> vehicleRepository.deleteVehicles(vehicleIds));
        } catch (Throwable throwable) {
            LOGGER.error("Delete vehicles error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
