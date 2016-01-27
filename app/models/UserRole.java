package models;

import be.objectify.deadbolt.core.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Anton Chernov on 12/31/2015.
 */
@Entity
@Table(name = "role")
public class UserRole implements Role {
    @Id
    public Long id;

    public String name;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
