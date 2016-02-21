package dto;

import models.Address;
import models.User;
import play.data.validation.Constraints;

import java.util.Objects;

/**
 * Created by Anton Chernov on 2/2/2016.
 */
public class AccountDTO {
    @Constraints.Required
    @Constraints.MaxLength(40)
    @Constraints.MinLength(5)
    public String username;

    @Constraints.MaxLength(40)
    public String name;

    @Constraints.Required
    public String surname;

    @Constraints.MaxLength(40)
    public String patronymic;

    @Constraints.MaxLength(40)
    public String birthday;

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.MaxLength(40)
    public String country;

    @Constraints.MaxLength(40)
    public String city;

    @Constraints.MaxLength(40)
    public String street;

    @Constraints.MaxLength(40)
    public String house;

    @Constraints.MaxLength(40)
    public String flat;

    public static AccountDTO getAccount(User user) {
        AccountDTO account = new AccountDTO();
        account.username = user.username;
        account.name = user.name;
        account.surname = user.surname;
        account.patronymic = user.patronymic;
        account.birthday = user.birthday;
        account.email = user.email;
        if (!Objects.isNull(user.address)) {
            account.country = user.address.country;
            account.city = user.address.city;
            account.street = user.address.street;
            account.house = user.address.house;
            account.flat = user.address.flat;
        }
        return account;
    }

   public static User getUser(AccountDTO account, User user) {
        user.username = account.username;
        user.name = account.name;
        user.surname = account.surname;
        user.patronymic = account.patronymic;
        user.birthday = account.birthday;
        user.email = account.email;
        if (!isAddressNull(account)) {
            user.address = new Address();
            user.address.country = account.country;
            user.address.city = account.city;
            user.address.street = account.street;
            user.address.house = account.house;
            user.address.flat = account.flat;
        }
        return user;
    }

    private static boolean isAddressNull(AccountDTO userDTO) {
        return Objects.isNull(userDTO.country) && Objects.isNull(userDTO.city)
                && Objects.isNull(userDTO.street) && Objects.isNull(userDTO.house) && Objects.isNull(userDTO.flat);
    }

}
