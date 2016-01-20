package repository;

import models.Company;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anton Chernov on 1/21/2016.
 */
public class CompanyRepository {
    public List<Company> page(long id, int count, boolean ascOrder) {
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT c FROM Company c WHERE ");
        if (ascOrder) {
            stringBuilder.append("c.id >= ? AND c.deleted = ? ORDER BY c.id ASC");
        } else {
            stringBuilder.append("c.id < ? AND c.deleted = ? ORDER BY c.id DESC");
        }
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter(1, id);
        query.setParameter(2, false);
        query.setMaxResults(count);
        List<Company> companies = query.getResultList();
        if (!ascOrder)
            Collections.reverse(companies);
        return companies;
    }
}
