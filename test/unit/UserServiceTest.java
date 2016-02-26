package unit;

import models.User;
import models.UserRole;
import org.junit.Test;
import play.Logger;
import play.test.FakeApplication;
import play.test.WithApplication;
import exception.ServiceException;
import service.UserService;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

/**
 * Created by Anton Chernov on 2/9/2016.
 */
public class UserServiceTest extends WithApplication {
    private static final Logger.ALogger LOGGER = Logger.of(UserServiceTest.class);

    @Override
    protected FakeApplication provideFakeApplication() {
        return fakeApplication(inMemoryDatabase("cargo_traffic"));
    }

    @Test
    public void testFindUser() throws ServiceException {
        UserService userService = new UserService();
        User user = userService.find(1);
        assertEquals(new Long(1), user.id);
    }

    @Test
    public void testSaveUser() throws ServiceException {
        UserService userService = new UserService();
        User user = new User();
        user.username = "username";
        user.name = "name";
        user.surname = "surname";
        user.patronymic = "patronymic";
        user.birthday = "1994-01-06";
        user.email = "email";
        user.userRoleList = new ArrayList<>();
        UserRole role = new UserRole();
        role.name = "ADMIN";
        user.userRoleList.add(role);
        user.password = "password";
        userService.save(user);
        assertNotNull(user.id);
    }
}
