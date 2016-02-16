package repository;


import models.Product;
import models.ProductInWaybill;
import models.User;
import models.statuses.WaybillStatus;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class WaybillRepository {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListRepository.class);

    public List<ProductInWaybill> getProducts(User user) {
        EntityManager em = JPA.em();
        TypedQuery<ProductInWaybill> query = em.createQuery("Select pwb From ProductInWaybill pwb JOIN pwb.product p WHERE" +
                " pwb.waybillVehicleDriver.waybill = (Select w From WaybillVehicleDriver wvd JOIN wvd.waybill w " +
                "WHERE wvd.driver = :user AND wvd.waybill.status = :status)",ProductInWaybill.class);
        query.setParameter("user", user);
        query.setParameter("status", WaybillStatus.TRANSPORTATION_STARTED);
        List<ProductInWaybill> list = query.getResultList();
        for(ProductInWaybill product: list){
            System.out.println(product.product.name+" "+ product.quantity);
        }
        return list;
    }
}
