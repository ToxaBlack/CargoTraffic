package repository;

import models.Company;
import models.Warehouse;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;


public class WarehouseRepository {
    public List<Warehouse> page(long id, int count, boolean ascOrder) {
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT w FROM Warehouse w WHERE ");
        if (ascOrder) {
            stringBuilder.append("w.id >= :id AND c.deleted = :deleted ORDER BY w.id ASC");
        } else {
            stringBuilder.append("w.id < :id AND c.deleted = :deleted ORDER BY w.id DESC");
        }
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter("id", id);
        query.setParameter("deleted", false);
        query.setMaxResults(count);
        List<Warehouse> warehouses = query.getResultList();
        if (!ascOrder)
            Collections.reverse(warehouses);
        return warehouses;
    }
}
