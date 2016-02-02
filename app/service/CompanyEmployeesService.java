package service;

import models.User;
import models.UserRole;
import play.Logger;
import play.db.jpa.JPA;
import repository.UserRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Olga on 25.01.2016.
 */
public class CompanyEmployeesService {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyEmployeesService.class);

    private UserRepository userRepository;

    public CompanyEmployeesService() {
        userRepository = new UserRepository();
    }

    public List<User> getEmployees(long companyId, long id, int count,  boolean ascOrder) throws ServiceException {
        try {
            return JPA.withTransaction(() -> userRepository.getList(companyId, id, count, ascOrder));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public User getEmployee(long id) throws ServiceException {
        try {
            return JPA.withTransaction(() -> userRepository.getUser(id));
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

    public void removeEmployees(List<Long> ids) throws ServiceException {
        try {
            JPA.withTransaction(() -> userRepository.removeUser(ids));
        } catch (Throwable throwable) {
            LOGGER.error("Remove employees error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
    public void updateEmployee(User employee) throws ServiceException {
        try {
            List<UserRole> roles = JPA.withTransaction(() -> userRepository.getRoleByName(employee.userRoleList.get(0).name));
            employee.setRoles(roles);
            JPA.withTransaction(() -> userRepository.update(employee));
        } catch (Throwable throwable) {
            LOGGER.error("Update employees error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
