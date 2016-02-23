package repository;

import models.FinancialHighlights;
import models.WaybillVehicleDriver;
import models.statuses.WaybillStatus;
import org.apache.commons.collections4.CollectionUtils;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MoneyRepository {
    private static final Logger.ALogger LOGGER = Logger.of(VehicleRepository.class);

    public List<FinancialHighlights> getFinancialHighlights(Date minDate, Date maxDate, long companyId) {
        LOGGER.debug("Get financial highlights: {}, {}, {}", minDate, maxDate, companyId);
        EntityManager em = JPA.em();
        TypedQuery<FinancialHighlights> query = em.createQuery("Select fh From FinancialHighlights fh " +
                "WHERE fh.deliveredDate >= :minDate " +
                "AND fh.deliveredDate < :maxDate " +
                "AND fh.waybillVehicleDriver.driver.company.id = :companyId",
                FinancialHighlights.class);
        query.setParameter("minDate",minDate);
        query.setParameter("maxDate",maxDate);
        query.setParameter("companyId",companyId);
        return query.getResultList();
    }

    public void saveFinancialHighlights(FinancialHighlights financialHighlights) {
        EntityManager em = JPA.em();
        em.persist(financialHighlights);
    }

    public void updateFinancialHighlights(FinancialHighlights financialHighlights) {
        EntityManager em = JPA.em();
        em.merge(financialHighlights);
    }

    public FinancialHighlights getFinancialHighlights(WaybillVehicleDriver wvd) {
        EntityManager em = JPA.em();
        TypedQuery<FinancialHighlights> query = em.createQuery("Select fh From FinancialHighlights fh" +
                " WHERE fh.waybillVehicleDriver = :wvd", FinancialHighlights.class);
        query.setParameter("wvd",wvd);
        List<FinancialHighlights> financialHighlightsList = query.getResultList();
        if (CollectionUtils.isNotEmpty(financialHighlightsList))
            return financialHighlightsList.get(0);
        return null;
    }
}
