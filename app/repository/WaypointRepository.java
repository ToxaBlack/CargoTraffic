package repository;

import models.Waybill;
import models.Waypoint;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Olga on 15.02.2016.
 */
public class WaypointRepository {
    public List<Waypoint> findByWaybill(Long id){
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT w FROM Waypoint w WHERE w.waybill.id = ?");
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter(1, id);
        List<Waypoint> list = query.getResultList();
        //TODO DELETE
        for (Waypoint point: list) {
            Long tempId =point.waybill.id;
            point.waybill = new Waybill();
            point.waybill.id = tempId;
        }
        return list;
    }
}
