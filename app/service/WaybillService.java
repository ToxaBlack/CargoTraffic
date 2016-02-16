package service;

import models.User;
import models.Waybill;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.WaybillRepository;

/**
 * Created by Maxim on 2/16/2016.
 */
public class WaybillService {
    private static final Logger.ALogger LOGGER = Logger.of(WaybillService.class);

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
}
