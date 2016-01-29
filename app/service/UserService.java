package service;

import models.User;
import play.Logger;
import play.db.jpa.JPA;
import repository.UserRepository;

import java.util.List;

/**
 * Created by Anton Chernov on 12/29/2015.
 */
public class UserService {
    private static final Logger.ALogger LOGGER = Logger.of(UserService.class);

    private UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    public List<User> getUserList() throws ServiceException {
        LOGGER.debug("Get user list");
        try {
            return JPA.withTransaction(() -> userRepository.findAll());
        } catch (Throwable throwable) {
            LOGGER.error("Get user list = {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public User find(long id) throws ServiceException {
        LOGGER.debug("Get user with id = {}", id);
        try {
            return JPA.withTransaction(() -> userRepository.find(id));
        } catch (Throwable throwable) {
            LOGGER.error("Find user id = {}, error = {}", id, throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }


    public User findByUsername(String name) throws ServiceException {
        LOGGER.debug("Get user with name = {}", name);
        try {
            return JPA.withTransaction(() -> userRepository.findByUsername(name));
        } catch (Throwable throwable) {
            LOGGER.error("Find user name = {}, error = {}", name, throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void update(User user) throws ServiceException {
        LOGGER.debug("Update user with id = {}", user.id);
        try {
            JPA.withTransaction(() -> userRepository.update(user));
        } catch (Throwable throwable) {
            LOGGER.error("Update user id = {}, error = {}", user.id, throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
