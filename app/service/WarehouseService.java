package service;


import models.Company;
import models.Warehouse;
import play.Logger;
import play.db.jpa.JPA;
import repository.CompanyRepository;
import repository.WarehouseRepository;

import java.util.List;

public class WarehouseService {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyService.class);

    private WarehouseRepository warehouseRepository;

    public WarehouseService() {
        warehouseRepository = new WarehouseRepository();
    }

    public List<Warehouse> getWarhouses(long id, int count, boolean ascOrder) throws ServiceException {
        LOGGER.debug("Get company list: {}, {}, {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> warehouseRepository.page(id, count, ascOrder));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
