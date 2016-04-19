package repository;


import models.*;
import models.statuses.PackingListStatus;
import models.statuses.WaybillStatus;
import org.apache.commons.collections4.CollectionUtils;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
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

    public static List<Vehicle> getVehicles(long companyId) {
        LOGGER.debug("Get vehicles: company {}", companyId);
        EntityManager em = JPA.em();
        String stringBuilder = "SELECT v FROM Vehicle v " +
                "LEFT JOIN FETCH v.vehicleType " +
                "WHERE  v.company.id = :companyId " +
                "AND v.deleted = false " +
                "AND (SELECT count(wvd.id) from WaybillVehicleDriver wvd " +
                "where v=wvd.vehicle AND wvd.status = 'TRANSPORTATION_STARTED') =0" +
                "ORDER BY v.id ASC ";
        Query query = em.createQuery(stringBuilder);
        query.setParameter("companyId", companyId);
        List<Vehicle> vehicles = query.getResultList();
        return vehicles;
    }

    public Waybill saveWaybill(Waybill waybill) {
        Set<WaybillVehicleDriver> wvds = waybill.vehicleDrivers;
        List<ProductInWaybill> piws;
        EntityManager em = JPA.em();
        em.persist(waybill);
        em.flush();
        em.refresh(waybill);

        waybill.packingList.status = PackingListStatus.TRANSPORTATION_STARTED;
        em.persist(waybill.packingList);
        em.flush();
        em.refresh(waybill.packingList);
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

    public List<ProductInWaybill> getWVDProducts(WaybillVehicleDriver wvd) {
        EntityManager em = JPA.em();
        TypedQuery<ProductInWaybill> query = em.createQuery("Select pwb From ProductInWaybill pwb JOIN FETCH pwb.product p WHERE" +
                " pwb.waybillVehicleDriver = :wvd AND pwb.quantity > 0", ProductInWaybill.class);
        query.setParameter("wvd", wvd);
        List<ProductInWaybill> list = query.getResultList();
        return list;
    }

    public WaybillVehicleDriver getWVDByDriver(User user) {
        EntityManager em = JPA.em();
        TypedQuery<WaybillVehicleDriver> query = em.createQuery("Select wvd From WaybillVehicleDriver wvd JOIN fetch wvd.waybill w " +
                "WHERE wvd.driver = :user AND wvd.waybill.status = :status ",WaybillVehicleDriver.class);
        query.setParameter("user", user);
        query.setMaxResults(1);
        query.setParameter("status", WaybillStatus.TRANSPORTATION_STARTED);
        WaybillVehicleDriver wvd = null;
        try {
            wvd = query.getSingleResult();
        } catch (NoResultException ex){
            LOGGER.error(ex.getMessage());
        }
        return wvd;
    }

    public void completeTransportationWVD(WaybillVehicleDriver wvd) {
        EntityManager em = JPA.em();
        wvd.status = WaybillStatus.TRANSPORTATION_COMPLETED;
        em.merge(wvd);
    }

    public void completeTransportationWaybill(Waybill waybill) {
        EntityManager em = JPA.em();
        waybill.status = WaybillStatus.TRANSPORTATION_COMPLETED;
        waybill.arrivalDate = new Date();
        em.merge(waybill);
    }

    public boolean IsCompleteAllDeliveries(Waybill waybill) {
        EntityManager em = JPA.em();
        TypedQuery<WaybillVehicleDriver> query = em.createQuery("Select wvd From WaybillVehicleDriver wvd" +
                " WHERE wvd.status = :status AND wvd.waybill = :waybill ",WaybillVehicleDriver.class);
        query.setParameter("waybill",waybill);
        query.setParameter("status",WaybillStatus.TRANSPORTATION_STARTED);
        return query.getResultList().size() == 0;
    }
}
