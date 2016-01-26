package service;


import models.Warehouse;
import play.Logger;
import play.db.jpa.JPA;
import repository.WarehouseRepository;

import java.util.List;

public class WarehouseService {
    private static final Logger.ALogger LOGGER = Logger.of(WarehouseService.class);

    private WarehouseRepository warehouseRepository;

    public WarehouseService() {
        warehouseRepository = new WarehouseRepository();
    }

    public List<Warehouse> getWarhouses(long id, int count, boolean ascOrder) throws ServiceException {
        LOGGER.debug("Get warehouse list: {}, {}, {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> warehouseRepository.page(id, count, ascOrder));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void deleteWarehouses(List<Long> idWarehouses) {
        warehouseRepository.deleteWarehouses(idWarehouses);
    }

    public void addWarehouse(Warehouse warehouse) throws ServiceException {
        try {
            JPA.withTransaction(() -> warehouseRepository.addWarehouse(warehouse));
        } catch (Throwable throwable) {
            LOGGER.error("Add warehouse with name = {}", warehouse.name);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public Warehouse editWarehouse(Warehouse warehouse) throws ServiceException {
        try {
            return JPA.withTransaction(() -> warehouseRepository.addWarehouse(warehouse));
        } catch (Throwable throwable) {
            LOGGER.error("Add warehouse with name = {}", warehouse.name);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
