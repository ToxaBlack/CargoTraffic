package repository;

import models.Waybill;
import models.Waypoint;
import models.statuses.WaybillStatus;
import models.statuses.WaypointStatus;
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
        for (Waypoint point: list) {
            Long tempId = point.waybill.id;
            point.waybill = new Waybill();
            point.waybill.id = tempId;
        }
        return list;
    }

    public void setChecked(List<Long> ids, boolean isChecked){
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("UPDATE Waypoint w SET w.status = ? WHERE w.id in (");
        for (Long id : ids) {
            stringBuilder.append(id);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        Query query = em.createQuery(stringBuilder.toString());
        if(isChecked) query.setParameter(1, WaypointStatus.valueOf("CHECKED"));
        else query.setParameter(1, WaypointStatus.valueOf("UNCHECKED"));
        query.executeUpdate();
    }
}
