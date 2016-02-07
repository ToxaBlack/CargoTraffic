package repository;

import models.PackingList;
<<<<<<< HEAD
=======
import models.Warehouse;
>>>>>>> bdb1015c23b6fe213247c52ae1ca5f4ec4ab056e
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
<<<<<<< HEAD
=======
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
>>>>>>> bdb1015c23b6fe213247c52ae1ca5f4ec4ab056e

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListRepository {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListRepository.class);

<<<<<<< HEAD
    public PackingList savePackingList(PackingList packingList) {
        EntityManager em = JPA.em();

        return packingList;
=======
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
>>>>>>> bdb1015c23b6fe213247c52ae1ca5f4ec4ab056e
    }
}
