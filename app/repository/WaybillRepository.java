package repository;


import models.*;
import models.statuses.WaybillStatus;
import org.apache.commons.collections4.CollectionUtils;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

/**
 * Created by Maxim on 2/16/2016.
 */
public class WaybillRepository {

    private static final Logger.ALogger LOGGER = Logger.of(WaybillRepository.class);

    public static Waybill getWaybill(long id, long companyId) {
        LOGGER.debug("Get waybill: {}, {}", id, companyId);
        EntityManager em = JPA.em();
        String sqlQuery = "SELECT wb FROM Waybill wb " +
                "LEFT JOIN FETCH wb.packingList " +
                "LEFT JOIN FETCH wb.waypoints " +
                "LEFT JOIN FETCH wb.vehicleDrivers " +
                "WHERE wb.manager.company.id = :companyId " +
                "AND wb.id = :id";
        Query query = em.createQuery(sqlQuery);
        query.setParameter("companyId", companyId);
        query.setParameter("id", id);
        query.setMaxResults(1);
        List<Waybill> waybills = query.getResultList();
        if (CollectionUtils.isEmpty(waybills))
            return new Waybill();
        return waybills.get(0);
    }


    public Waybill saveWaybill(Waybill waybill) {
        Set<WaybillVehicleDriver> wvds = waybill.vehicleDrivers;
        List<ProductInWaybill> piws;
        EntityManager em = JPA.em();
        em.persist(waybill);
        em.flush();
        em.refresh(waybill);
        for(WaybillVehicleDriver wvd : wvds)
        {
            piws = wvd.productsInWaybill;
            em.persist(wvd);
            em.flush();
            em.refresh(wvd);
            for(ProductInWaybill piw : piws){
                em.persist(piw);
                em.flush();
                em.refresh(piw);
            }
        }
        return waybill;
    }

    public List<ProductInWaybill> getProducts(User user) {
        EntityManager em = JPA.em();
        TypedQuery<ProductInWaybill> query = em.createQuery("Select pwb From ProductInWaybill pwb JOIN pwb.product p WHERE" +
                " pwb.waybillVehicleDriver.waybill = (Select w From WaybillVehicleDriver wvd JOIN wvd.waybill w " +
                "WHERE wvd.driver = :user AND wvd.waybill.status = :status)", ProductInWaybill.class);
        query.setParameter("user", user);
        query.setParameter("status", WaybillStatus.TRANSPORTATION_STARTED);
        List<ProductInWaybill> list = query.getResultList();
        return list;
    }
}
