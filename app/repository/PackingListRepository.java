package repository;

import models.*;
import models.statuses.PackingListStatus;
import org.apache.commons.collections4.CollectionUtils;
import play.Logger;
import play.db.jpa.JPA;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListRepository {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListRepository.class);


    public PackingList savePackingList(PackingList packingList) {
        EntityManager em = JPA.em();
        em.persist(packingList);
        em.flush();
        em.refresh(packingList);
        return packingList;
    }

    public ProductInPackingList saveProductInPackingList( ProductInPackingList packingList) {
        EntityManager em = JPA.em();
        em.persist(packingList);
        em.flush();
        em.refresh(packingList);
        return packingList;
    }


    public List<PackingList> getPackingLists(long id, int count, boolean ascOrder, Long companyId) {
        LOGGER.debug("Get packingLists: {}, {}, {}, {}", id, count, ascOrder, companyId);
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT pl FROM PackingList pl WHERE pl.status = ? AND ");
        if (ascOrder) {
            stringBuilder.append("pl.id >= ? AND pl.dispatcher.company.id = ? ORDER BY pl.id ASC");
        } else {
            stringBuilder.append("pl.id < ? AND pl.dispatcher.company.id = ? ORDER BY pl.id DESC");
        }
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter(1, PackingListStatus.CREATED);
        query.setParameter(2, id);
        query.setParameter(3, companyId);
        query.setMaxResults(count);
        List<PackingList> packingLists = query.getResultList();
        if (CollectionUtils.isEmpty(packingLists))
            return new ArrayList<>();
        if (!ascOrder)
            Collections.reverse(packingLists);
        return packingLists;
    }

    public List<PackingList> getDispatcherPackingLists(long id, int count, boolean ascOrder, User user) {
        LOGGER.debug("Get packingLists for dispatcher: {}, {}, {}, {}", id, count, ascOrder, user);
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT pl FROM PackingList pl WHERE pl.status = :status" +
                " AND pl.dispatcher = :user AND ");
        if (ascOrder) {
            stringBuilder.append("pl.id >= :plId AND pl.dispatcher.company.id = :companyId ORDER BY pl.id ASC");
        } else {
            stringBuilder.append("pl.id < :plId AND pl.dispatcher.company.id = :companyId ORDER BY pl.id DESC");
        }
        Query query = em.createQuery(stringBuilder.toString());

        query.setParameter("status", PackingListStatus.CREATED);
        query.setParameter("plId", id);
        query.setParameter("companyId", user.company.id);
        query.setParameter("user",user);
        query.setMaxResults(count);
        List<PackingList> packingLists = query.getResultList();
        if (CollectionUtils.isEmpty(packingLists))
            return new ArrayList<>();
        if (!ascOrder)
            Collections.reverse(packingLists);
        return packingLists;
    }

    public PackingList getPackingList(long id, Long companyId, PackingListStatus status) {
        LOGGER.debug("Get packingList: {}, {}", id, companyId);
        EntityManager em = JPA.em();
        String sqlQuery = new StringBuilder()
                .append("SELECT pl FROM PackingList pl ")
                .append("LEFT JOIN FETCH pl.departureWarehouse ")
                .append("LEFT JOIN FETCH pl.destinationWarehouse ")
                .append("WHERE pl.dispatcher.company.id = :companyId ")
                .append(status != null ? "AND pl.status = :status " : "")
                .append("AND pl.id = :id").toString();
        Query query = em.createQuery(sqlQuery);
        query.setParameter("companyId", companyId);
        if(status != null) query.setParameter("status", status);
        query.setParameter("id", id);
        query.setMaxResults(1);
        List<PackingList> packingLists = query.getResultList();
        if (CollectionUtils.isEmpty(packingLists))
            return new PackingList();
        return packingLists.get(0);
    }
}
