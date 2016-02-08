package repository;

import models.*;
import play.Logger;
import play.db.jpa.JPA;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListRepository {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListRepository.class);


    public PackingList savePackingList(PackingList packingList) {
        EntityManager em = JPA.em();
        //TODO
        /*for(ProductInPackingList product: packingList.productsInPackingList){
            product.product.measureUnit = getMeasureUnit( product.product.measureUnit.name).get(0);
            product.product.storageType = getStorageType( product.product.storageType.type).get(0);
        }*/
        em.persist(packingList);
        em.flush();
        em.refresh(packingList);
        return packingList;
    }

    public List<MeasureUnit> getMeasureUnit(String name){
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT u FROM MeasureUnit u WHERE u.name = ?");
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter(1, name);
        return query.getResultList();
    }
    public List<StorageType> getStorageType(String type){
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT t FROM StorageType t WHERE t.type = ?");
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter(1, type);
        return query.getResultList();
    }
    public List<PackingList> getForCheckPackingLists(long id, int count, boolean ascOrder) {
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT pl FROM PackingList pl WHERE pl.status = 'СREATED' AND ");
        if (ascOrder) {
            stringBuilder.append("pl.id >= :id ORDER BY pl.id ASC");
        } else {
            stringBuilder.append("pl.id < :id ORDER BY pl.id DESC");
        }
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter("id", id);
        query.setMaxResults(count);
        List<PackingList> packingLists = query.getResultList();
        if (!ascOrder)
            Collections.reverse(packingLists);
        return packingLists;
    }
}
