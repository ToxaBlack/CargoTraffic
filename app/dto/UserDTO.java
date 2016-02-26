package dto;

import models.User;

/**
 * Created by Maxim on 2/16/2016.
 */
public class UserDTO {
    public Long id;

    public String username;

    public String name;

    public String surname;

    public String patronymic;

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.id = user.id;
        userDTO.username = user.username;
        userDTO.name = user.name;
        userDTO.surname = user.surname;
        userDTO.patronymic = user.patronymic;
        return userDTO;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserDTO that = (UserDTO) obj;

        return (id != null ? id.equals(that.id) : that.id == null)
                && (username != null ? username.equals(that.username) : that.username == null)
                && (name != null ? name.equals(that.name) : that.name == null)
                && (surname != null ? surname.equals(that.surname) : that.surname == null)
                && (patronymic != null ? patronymic.equals(that.patronymic) : that.patronymic == null);
    }
}
