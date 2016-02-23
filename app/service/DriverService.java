package service;


import exception.ServiceException;
import models.*;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.MoneyRepository;
import repository.PackingListRepository;
import repository.ProductRepository;
import repository.WaybillRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class DriverService {
    private static final Logger.ALogger LOGGER = Logger.of(DriverService.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private WaybillRepository waybillRepository;

    @Inject
    private PackingListRepository packingListRepository;

    @Inject
    private MoneyRepository moneyRepository;

    public void createActOfLost(List<LostProduct> productList) throws ServiceException {
        LOGGER.debug("Create act of lost(service)");
        for(LostProduct lostProduct: productList) {
            try {
                JPA.withTransaction(() -> productRepository.saveActOfLost(lostProduct));
            } catch (Throwable throwable) {
                LOGGER.error("Get list error = {}", throwable.getMessage());
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
                return wvd.productsInWaybill;
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get products of waybill error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void completeDelivery(User user) throws ServiceException {
        LOGGER.debug("Complete delivery of {}", user);
        try {
            JPA.withTransaction(() -> {
                WaybillVehicleDriver wvd = waybillRepository.getWVDByDriver(user);
                waybillRepository.completeTransportationWVD(wvd);
                Waybill waybill = wvd.waybill;
                List<LostProduct> lostProducts = productRepository.getLostProducts(user);
                FinancialHighlights financialHighlights = moneyRepository.getFinancialHighlights(wvd);
                Double totalLostProductsCost = 0d;
                if (Objects.nonNull(lostProducts)) {
                    for (LostProduct lostProduct : lostProducts) {
                        totalLostProductsCost += lostProduct.product.price * lostProduct.quantity;
                    }
                }
                financialHighlights.productsLoss = totalLostProductsCost;
                financialHighlights.profit = financialHighlights.transportationIncome -
                        totalLostProductsCost - financialHighlights.vehicleFuelLoss;
                moneyRepository.updateFinancialHighlights(financialHighlights);

                //Check whether all cargo is delivered, then end up delivery
                if(waybillRepository.IsCompleteAllDeliveries(waybill)) {
                    waybillRepository.completeTransportationWaybill(waybill);

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
            LOGGER.error("Get list error = {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
