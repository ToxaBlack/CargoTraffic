package service;


import exception.ServiceException;
import models.Warehouse;
import play.Logger;
import play.db.jpa.JPA;
import repository.WarehouseRepository;

import javax.inject.Inject;
import java.util.List;

public class WarehouseService {
    private static final Logger.ALogger LOGGER = Logger.of(WarehouseService.class);

    @Inject
    private WarehouseRepository warehouseRepository;

    public List<Warehouse> getWarhouses(long id, int count, boolean ascOrder) throws ServiceException {
        LOGGER.debug("Get warehouse list: {}, {}, {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> warehouseRepository.getWarehouses(id, count, ascOrder));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void removeWarehouses(List<Warehouse> warehouses) throws ServiceException {
        try {
            JPA.withTransaction(() -> warehouseRepository.removeWarehouses(warehouses));
        } catch (Throwable throwable) {
            LOGGER.error("Remove warehouses error");
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public Warehouse addWarehouse(Warehouse warehouse) throws ServiceException {
        try {
            return JPA.withTransaction(() -> warehouseRepository.addWarehouse(warehouse));
        } catch (Throwable throwable) {
            LOGGER.error("Add warehouse with name = {}", warehouse.name);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public Warehouse editWarehouse(Warehouse warehouse) throws ServiceException {
        try {
            return JPA.withTransaction(() -> warehouseRepository.editWarehouse(warehouse));
        } catch (Throwable throwable) {
            LOGGER.error("Edit warehouse with name = {}", warehouse.name);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
