package service;

import models.Company;
import play.Logger;
import play.db.jpa.JPA;
import repository.CompanyRepository;

import java.util.Arrays;
import java.util.List;


public class CompanyService {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyService.class);

    private CompanyRepository companyRepository;

    public CompanyService() {
        companyRepository = new CompanyRepository();
    }

    public List<Company> getCompanies(long id, int count, boolean ascOrder) throws ServiceException {
        LOGGER.debug("Get company list: {}, {}, {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> companyRepository.getPage(id, count, ascOrder));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void lockCompanies(List<Long> companyIds) throws ServiceException {
        LOGGER.debug("Lock companies: {}", Arrays.toString(companyIds.toArray()));
        try {
            JPA.withTransaction(() -> companyRepository.updateCompanyStatus(companyIds, true));
        } catch (Throwable throwable) {
            LOGGER.error("Lock companies error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void unlockCompanies(List<Long> companyIds) throws ServiceException {
        LOGGER.debug("Unlock companies: {}", Arrays.toString(companyIds.toArray()));
        try {
            JPA.withTransaction(() -> companyRepository.updateCompanyStatus(companyIds, false));
        } catch (Throwable throwable) {
            LOGGER.error("Unlock companies error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
