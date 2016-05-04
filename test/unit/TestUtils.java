package unit;

import models.User;
import models.UserRole;

import java.util.ArrayList;

/**
 * Created by Anton on 05.05.2016.
 */
public class TestUtils {

    static public User getUser() {
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
        return user;
    }
}
