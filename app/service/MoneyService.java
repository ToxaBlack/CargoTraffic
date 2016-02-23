package service;

import exception.ServiceException;
import models.FinancialHighlights;
import models.User;
import models.Vehicle;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.MoneyRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Created by dmitriy on 23.2.16.
 */
public class MoneyService {
    private static final Logger.ALogger LOGGER = Logger.of(MoneyService.class);

    @Inject
    MoneyRepository moneyRepository;

    public List<FinancialHighlights> getFinancialHighlights(Date minDate, Date maxDate) throws ServiceException {
        LOGGER.debug("Get financial highlights: {}, {}", minDate, maxDate);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                return moneyRepository.getFinancialHighlights(minDate, maxDate, user.company.id);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get hinancial highlights error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
