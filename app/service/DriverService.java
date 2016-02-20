package service;


import models.*;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.PackingListRepository;
import repository.ProductRepository;
import repository.WaybillRepository;

import javax.inject.Inject;
import java.util.List;

public class DriverService {
    private static final Logger.ALogger LOGGER = Logger.of(DriverService.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private WaybillRepository waybillRepository;

    @Inject
    private PackingListRepository packingListRepository;

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
                WaybillVehicleDriver wvd = waybillRepository.getWVDByDriver(user);
                return waybillRepository.getWaybillProducts(wvd.waybill);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get products of waybill error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void completeDelivery(User user) throws ServiceException {
        LOGGER.debug("Complete delivery of {}", user);
        try {
            JPA.withTransaction(() -> {
                WaybillVehicleDriver wvd = waybillRepository.getWVDByDriver(user);
                waybillRepository.completeTransporationWaybill(wvd);
                Waybill waybill = wvd.waybill;
                if(waybillRepository.IsCompleteAllDeliveries(waybill)) {
                    PackingList packingList = waybill.packingList;
                    packingListRepository.completeTransporationPackList(packingList);
                    for(ProductInPackingList product: packingList.productsInPackingList) {
                        long missed = productRepository.findOutCountOfMiss(product.product);
                        product.count -= missed;
                        productRepository.saveAfterDelivery(product);
                    }
                }
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
