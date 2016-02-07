package repository;

import models.PackingList;
import models.Warehouse;
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
