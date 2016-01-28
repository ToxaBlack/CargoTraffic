package models;

import be.objectify.deadbolt.core.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;

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
