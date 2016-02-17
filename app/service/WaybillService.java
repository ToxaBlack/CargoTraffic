package service;


import models.ProductInWaybill;
import models.User;
import models.Waybill;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.WaybillRepository;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by Maxim on 2/16/2016.
 */
public class WaybillService {
    private static final Logger.ALogger LOGGER = Logger.of(WaybillService.class);

    @Inject
    private WaybillRepository repository;

    public Waybill getWaybill(long id) throws ServiceException {
        LOGGER.debug("API get waybill: {}", id);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                return WaybillRepository.getWaybill(id, user.company.id);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get packingList error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }

    }

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
