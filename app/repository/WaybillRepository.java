package repository;


import models.Product;
import models.ProductInWaybill;
import models.User;
import models.statuses.WaybillStatus;
import models.Waybill;
import org.apache.commons.collections4.CollectionUtils;
import play.Logger;
import play.db.jpa.JPA;
import javax.persistence.TypedQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Maxim on 2/16/2016.
 */
public class WaybillRepository {

    private static final Logger.ALogger LOGGER = Logger.of(WaybillRepository.class);

    public static Waybill getWaybill(long id, long companyId) {
        LOGGER.debug("Get waybill: {}, {}", id, companyId);
        EntityManager em = JPA.em();
        String sqlQuery = "SELECT wb FROM Waybill wb " +
                "left join fetch wb.packingList " +
                "left join fetch wb.waypointList " +
                "left join fetch wb.vehicleDriverList " +
                "WHERE wb.manager.company.id = :companyId " +
                "AND wb.id = :id";
        Query query = em.createQuery(sqlQuery);
        query.setParameter("companyId", companyId);
        query.setParameter("id", id);
        query.setMaxResults(1);
        List<Waybill> packingLists = query.getResultList();
        if (CollectionUtils.isEmpty(packingLists))
            return new Waybill();
        return packingLists.get(0);
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
