package dto;

import com.fasterxml.jackson.annotation.JsonView;
import models.Address;
import models.Company;
import models.User;
import models.UserRole;

import java.util.List;
import java.util.Objects;

/**
 * Created by Olga on 27.01.2016.
 */
public class UserDTO {
    @JsonView(Views.Account.class)
    public String id;
    @JsonView(Views.Account.class)
    public String username;
    @JsonView(Views.Account.class)
    public String name;
    @JsonView(Views.Account.class)
    public String surname;
    @JsonView(Views.Account.class)
    public String patronymic;
    @JsonView(Views.Account.class)
    public String birthday;
    @JsonView(Views.Account.class)
    public String email;
    @JsonView(Views.Account.class)
    public String country;
    @JsonView(Views.Account.class)
    public String city;
    @JsonView(Views.Account.class)
    public String street;
    @JsonView(Views.Account.class)
    public String house;
    @JsonView(Views.Account.class)
    public String flat;


    @JsonView(Views.Employee.class)
    public String password;
    @JsonView(Views.Employee.class)
    public String roles;

    public static UserDTO getUser(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.username = user.username;
        userDTO.name = user.name;
        userDTO.surname = user.surname;
        userDTO.patronymic = user.patronymic;
        userDTO.birthday = user.birthday;
        userDTO.email = user.email;
        if(!Objects.isNull(user.address)){
            userDTO.country = user.address.country;
            userDTO.city = user.address.city;
            userDTO.street = user.address.street;
            userDTO.house = user.address.house;
            userDTO.flat = user.address.flat;
        }
        userDTO.password = "";
        return userDTO;
    }
}
