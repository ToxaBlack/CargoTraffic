package repository;

import models.Company;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anton Chernov on 1/21/2016.
 */
public class CompanyRepository {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyRepository.class);

    public List<Company> getPage(long id, int count, boolean ascOrder) {
        LOGGER.debug("Get page: {}, {}, {}", id, count, ascOrder);
        EntityManager em = JPA.em();
        StringBuilder stringBuilder = new StringBuilder("SELECT c FROM Company c WHERE ");
        if (ascOrder) {
            stringBuilder.append("c.id >= ? ORDER BY c.id ASC");
        } else {
            stringBuilder.append("c.id < ? ORDER BY c.id DESC");
        }
        Query query = em.createQuery(stringBuilder.toString());
        query.setParameter(1, id);
        query.setMaxResults(count);
        List<Company> companies = query.getResultList();
        if (!ascOrder)
            Collections.reverse(companies);
        return companies;
    }

    public void updateCompanyStatus(List<Long> companyIds, boolean isLock) {
        LOGGER.debug("Update companies status: {}, {}", Arrays.toString(companyIds.toArray()), isLock);
        EntityManager em = JPA.em();

        StringBuilder stringBuilder = new StringBuilder("UPDATE Company c SET c.deleted=? WHERE c.id in (");
        for (Long id : companyIds) {
            stringBuilder.append(id);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");

        Query query = em.createQuery(stringBuilder.toString());
        if (isLock)
            query.setParameter(1, true);
        else
            query.setParameter(1, false);
        query.executeUpdate();
    }

    public boolean isCompanyAlreadyPresent(Company company) {
        return false;
    }

    public Company addCompany(Company company) {
        return null;
    }
}
