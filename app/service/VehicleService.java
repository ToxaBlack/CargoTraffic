package service;

import models.User;
import models.Vehicle;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.CompanyRepository;
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
    @Inject
    CompanyRepository companyRepository;

    public List<Vehicle> getVehicles(Long id, Integer count, Boolean ascOrder) throws ServiceException {
        LOGGER.debug("Get vehicles id, count, ascOrder: {}, {}, {}", id, count, ascOrder);
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
                vehicle.vehicleType = vehicleRepository.getVehicleTypeByName(vehicle.vehicleType.vehicleType);
                vehicle.vehicleFuel = vehicleRepository.getVehicleFuelByName(vehicle.vehicleFuel.fuelName);
                vehicle.company = companyRepository.findCompanyByName(user.company.name);
                vehicle.deleted = false;
                return vehicleRepository.addVehicle(vehicle);
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
            return JPA.withTransaction(() -> {
                vehicle.vehicleType = vehicleRepository.getVehicleTypeByName(vehicle.vehicleType.vehicleType);
                vehicle.vehicleFuel = vehicleRepository.getVehicleFuelByName(vehicle.vehicleFuel.fuelName);
                return vehicleRepository.updateVehicle(vehicle);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Update vehicle error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void deleteVehicles(List<Long> vehicleIds) throws ServiceException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API delete vehicles: {}, {}", oldUser.toString(), Arrays.toString(vehicleIds.toArray()));
        try {
            JPA.withTransaction(() -> vehicleRepository.deleteVehicles(vehicleIds));
        } catch (Throwable throwable) {
            LOGGER.error("Delete vehicles error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
