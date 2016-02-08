package repository;

import models.Warehouse;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;


public class WarehouseRepository {
    public List<Warehouse> getWarehouses(long id, int count, boolean ascOrder) {
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT w FROM Warehouse w WHERE w.deleted = FALSE AND ");
        if (ascOrder) {
            stringBuilder.append("w.id >= :id ORDER BY w.id ASC");
        } else {
            stringBuilder.append("w.id < :id ORDER BY w.id DESC");
        }
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter("id", id);
        query.setMaxResults(count);
        List<Warehouse> warehouses = query.getResultList();
        if (!ascOrder)
            Collections.reverse(warehouses);
        return warehouses;
    }

    public void removeWarehouses(List<Warehouse> warehouses) {
        for(Warehouse warehouse: warehouses) {
            deleteWarehouse(warehouse);
        }
    }

    private void deleteWarehouse(Warehouse warehouse) {
        EntityManager em = JPA.em();
        warehouse.deleted = true;
        em.merge(warehouse);
    }

    public Warehouse addWarehouse(Warehouse warehouse) {
        EntityManager em = JPA.em();
        em.persist(warehouse);
        em.refresh(warehouse);
        return warehouse;
         //em.flush();
    }

    public Warehouse editWarehouse(Warehouse warehouse) {
        EntityManager entityManager = JPA.em();
        entityManager.merge(warehouse);
        return warehouse;
    }
}
