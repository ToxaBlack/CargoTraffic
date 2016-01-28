package repository;

import models.Company;
import org.apache.commons.collections4.CollectionUtils;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    public boolean isCompanyAlreadyExists(Company company) {
        LOGGER.debug("Is company present: {}", company.name);
        return this.findCompanyByName(company.name) != null;
    }

    public Company findCompanyByName(String name) {
        LOGGER.debug("Find company by name: {}", name);
        EntityManager em = JPA.em();
        String selectCompanySql = new String("SELECT c FROM Company c WHERE c.name=?");
        Query query = em.createQuery(selectCompanySql);
        query.setParameter(1, name);
        List<Company> companies = query.getResultList();
        if (CollectionUtils.isNotEmpty(companies)) return companies.get(0);
        return null;
    }

    public Company addCompany(Company company) {
        LOGGER.debug("Adding company: {}", company.name);
        EntityManager em = JPA.em();
        em.persist(company);
        em.refresh(company);
        return company;
    }
}
