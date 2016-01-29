package service;

import models.Company;
import models.User;
import models.UserRole;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.CompanyRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CompanyService {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyService.class);

    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private AccountService accountService;


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

    public Company addClient(Company company, User admin) throws ServiceException {
        LOGGER.debug("Adding client: {}, {}", company.name, admin.surname);
        try {
            company.locked = false;
            admin.deleted = false;
            UserRole userRole = new UserRole();
            userRole.name = "ADMIN";
            List<UserRole> userRoles = new ArrayList<>();
            userRoles.add(userRole);
            admin.setUserRoleList(userRoles);
            admin.username = accountService.generateUsername(company, admin);
            String password = accountService.generatePassword();
            User user = (User) Http.Context.current().args.get("user");
            EmailAttributes emailAttributes = new EmailAttributes();
            emailAttributes.setEmailText("Username: " + admin.username +
                    "\r\nPassword: " + password + "\r\nChange your password for more security.");
            emailAttributes.setEmailTitle("Login data");
            emailAttributes.setFromEmail(user.email);
            emailAttributes.setRecipientEmail(admin.email);
            EmailService.sendEmail(emailAttributes);
            admin.password = accountService.getPasswordHash(password);
            return JPA.withTransaction(() -> {
                boolean isExists = companyRepository.isCompanyAlreadyExists(company);
                if (!isExists) {
                    companyRepository.addCompany(company);
                    Company savedCompany = companyRepository.findCompanyByName(company.name);
                    admin.company = savedCompany;
                    // TODO userRepository.addUser(admin);
                    return savedCompany;
                } else
                    return null;
            });
        } catch (Throwable throwable) {
            LOGGER.error("Unlock companies error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
