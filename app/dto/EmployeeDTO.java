package dto;

import models.Address;
import models.Company;
import models.User;
import models.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Anton Chernov on 2/2/2016.
 */
public class EmployeeDTO extends AccountDTO {
    public String id;
    public String password;
    public String roles;

    public static User getUser(EmployeeDTO userDTO, User user) {
        AccountDTO.getUser(userDTO, user);
        if(StringUtils.isNotEmpty(userDTO.roles))
        {
            user.userRoleList = new ArrayList<>();
            UserRole role = new UserRole();
            role.name = userDTO.roles.toUpperCase();
            user.userRoleList.add(role);
        }
        if(StringUtils.isNotEmpty(userDTO.password)) user.password = BCrypt.hashpw(userDTO.password, BCrypt.gensalt());
        user.company = new Company();
        user.deleted = false;
        return user;
    }

}
