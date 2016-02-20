package repository;

import models.LostProduct;
import models.MeasureUnit;
import models.Product;
import models.ProductInPackingList;
import models.statuses.ProductStatus;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Olga on 08.02.2016.
 */
public class ProductRepository {

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

    public long findOutCountOfMiss(Product product) {
        EntityManager em = JPA.em();
        TypedQuery<LostProduct> query = em.createQuery("Select lp from LostProduct lp WHERE lp.product = :product",
                LostProduct.class);
        query.setParameter("product",product);
        LostProduct lostProduct = query.getSingleResult();
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
