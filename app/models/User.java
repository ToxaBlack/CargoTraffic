package models;

import be.objectify.deadbolt.core.models.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Anton Chernov on 12/26/2015.
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user")
public class User implements Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @ManyToOne
    @JsonIgnore
    public Company company;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    public Address address;

    public String username;

    public String name;

    public String surname;

    @JsonIgnore
    public String password;

    public String patronymic;

    public String email;

    public String birthday;

    @JsonIgnore
    public Boolean deleted;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="user_role",
    joinColumns =
            {@JoinColumn(name="user_id")},
    inverseJoinColumns =
            {@JoinColumn(name="role_id")})
    public List<UserRole> userRoleList;

    @Override
    public List<UserRole> getRoles() {
        return userRoleList;
    }

    public void setRoles(List<UserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    @JsonIgnore
    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getIdentifier() {
        return null;
    }


    @Override
    public String toString() {
        return username;
    }

    public User() {
    }


}
