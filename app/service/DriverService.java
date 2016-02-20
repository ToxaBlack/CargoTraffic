package service;


import models.LostProduct;
import models.ProductInWaybill;
import models.User;
import models.Waybill;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.ProductRepository;
import repository.WaybillRepository;

import javax.inject.Inject;
import java.util.List;

public class DriverService {
    private static final Logger.ALogger LOGGER = Logger.of(WaybillService.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private WaybillRepository waybillRepository;

    public void createActOfLost(List<LostProduct> productList) throws ServiceException {
        LOGGER.debug("Create act of lost(service)");
        for(LostProduct lostProduct: productList) {
            try {
                JPA.withTransaction(() -> productRepository.saveActOfLost(lostProduct));
            } catch (Throwable throwable) {
                LOGGER.error("Get list error = {}", throwable);
                throw new ServiceException(throwable.getMessage(), throwable);
            }
        }

    }

    public List<ProductInWaybill> getProducts() throws ServiceException {
        LOGGER.debug("Get products of waybill");
        User user = (User) Http.Context.current().args.get("user");
        try {
            return JPA.withTransaction(() -> {
                Waybill waybill = waybillRepository.getWaybillByDriver(user);
                return waybillRepository.getWaybillProducts(waybill);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get products of waybill error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
