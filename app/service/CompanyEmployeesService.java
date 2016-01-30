package service;

import models.User;
import play.Logger;
import play.db.jpa.JPA;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Olga on 25.01.2016.
 */
public class CompanyEmployeesService {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyEmployeesService.class);

    @Inject
    private UserRepository userRepository;

    public List<User> getCompanyEmployees(long companyId, long id, int count,  boolean ascOrder) throws ServiceException {
        LOGGER.debug("Get company employees list: {}, {}, {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> userRepository.getUserForEmployeesPage(companyId, id, count, ascOrder));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void addEmployee(User employee) throws ServiceException {
        try {
            JPA.withTransaction(() -> userRepository.addUser(employee));
        } catch (Throwable throwable) {
            LOGGER.error("Add employee with name = {}", employee.name);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
