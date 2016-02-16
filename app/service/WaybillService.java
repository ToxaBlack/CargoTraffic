package service;


import models.Product;
import models.ProductInWaybill;
import models.User;
import models.Waybill;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.WaybillRepository;

import javax.inject.Inject;
import java.util.List;

public class WaybillService {
    private static final Logger.ALogger LOGGER = Logger.of(WarehouseService.class);

    @Inject
    private WaybillRepository repository;

    public List<ProductInWaybill> getProducts() throws ServiceException {
        LOGGER.debug("Get products of waybill");
        User user = (User) Http.Context.current().args.get("user");
        try {
            return JPA.withTransaction(() -> repository.getProducts(user));
        } catch (Throwable throwable) {
            LOGGER.error("Get products of waybill error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

}
