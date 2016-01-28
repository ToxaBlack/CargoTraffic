package models;

import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by Anton Chernov on 12/30/2015.
 */

@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public Boolean locked;
}
