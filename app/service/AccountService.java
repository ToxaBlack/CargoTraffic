package service;

import exception.ServiceException;
import models.Company;
import models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.db.jpa.JPA;
import repository.UserRepository;

import javax.inject.Inject;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Created by dmitriy on 28.1.16.
 */
public class AccountService {
    private static final Logger.ALogger LOGGER = Logger.of(AccountService.class);

    @Inject
    private UserRepository userRepository;

    private static final char[] symbols;
    private static final Integer PASSWORD_LENGTH = 10;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    public String generateUsername(Company company, User user) throws ServiceException {
        try {
            return JPA.withTransaction(() -> {
                StringBuilder checkedUsername = new StringBuilder(company.name + "_" + user.surname + "_0");
                int i = 0;
                User savedUser;
                do {
                    i++;
                    checkedUsername.deleteCharAt(checkedUsername.length() - 1);
                    checkedUsername.append(i);
                    savedUser = userRepository.findByUsername(checkedUsername.toString());
                } while (!Objects.isNull(savedUser));
                return checkedUsername.toString();
            });
        } catch (Throwable throwable) {
            LOGGER.error("Cannot generate username: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public String generatePassword() throws ServiceException {
        String password = RandomStringUtils.random(PASSWORD_LENGTH, 0, symbols.length, true, true, symbols, new SecureRandom());
        return password;
    }

    public String getPasswordHash(String password) throws ServiceException {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
