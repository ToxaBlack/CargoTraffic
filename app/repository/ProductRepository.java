package repository;

import models.*;
import models.statuses.ProductStatus;
import models.statuses.WaybillStatus;
import play.Logger;
import play.db.jpa.JPA;
import service.DriverService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Olga on 08.02.2016.
 */
public class ProductRepository {
    private static final Logger.ALogger LOGGER = Logger.of(ProductRepository.class);

    public Product save(Product product){
        EntityManager em = JPA.em();
        List<MeasureUnit> units = getMeasureUnit(product.measureUnit.name);
        if(!units.isEmpty()) product.measureUnit = units.get(0);
        else {
            em.persist(product.measureUnit);
            em.flush();
            em.refresh(product.measureUnit);
        }
        em.persist(product);
        em.flush();
        em.refresh(product);
        return product;
    }

    public LostProduct saveActOfLost(LostProduct product) {
        EntityManager em = JPA.em();
        em.persist(product);
        em.flush();
        em.refresh(product);
        return product;
    }

    public List<LostProduct> getLostProducts(User driver) {
        EntityManager em = JPA.em();
        TypedQuery<LostProduct> query = em.createQuery("Select lp From LostProduct lp, WaybillVehicleDriver wvd " +
                "WHERE wvd.waybill.status = :status " +
                "AND wvd.driver = :driver " +
                "AND lp.driver = :driver",
                LostProduct.class);
        query.setParameter("status", WaybillStatus.TRANSPORTATION_STARTED);
        query.setParameter("driver",driver);
        return query.getResultList();
    }

    public long findOutCountOfMiss(Product product) {       // TODO status unnecessary?
        EntityManager em = JPA.em();
        TypedQuery<LostProduct> query = em.createQuery("Select lp from LostProduct lp WHERE lp.product = :product",
                LostProduct.class);
        query.setParameter("product",product);
        LostProduct lostProduct = null;
        try {
            lostProduct = query.getSingleResult();
        } catch (NoResultException ex){}
        return lostProduct == null ? 0 : lostProduct.quantity;
    }

    public void saveAfterDelivery(ProductInPackingList product) {
        EntityManager em = JPA.em();
        product.status = ProductStatus.DELIVERED;
        em.merge(product);
    }

    public List<MeasureUnit> getMeasureUnit(String name){
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT u FROM MeasureUnit u WHERE u.name = ?");
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter(1, name);
        return query.getResultList();
    }

}
