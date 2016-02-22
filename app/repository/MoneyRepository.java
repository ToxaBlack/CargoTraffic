package repository;

import models.FinancialHighlights;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


public class MoneyRepository {
    private static final Logger.ALogger LOGGER = Logger.of(VehicleRepository.class);

    public List<FinancialHighlights> getFinancialHighlights(Long minDate, Long maxDate, long companyId) {
        LOGGER.debug("Get financial highlights: {}, {}, {}", minDate, maxDate, companyId);
        EntityManager em = JPA.em();

        return new ArrayList<FinancialHighlights>();
    }
}
