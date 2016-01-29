package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "warehouse")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Constraints.Required
    public long id;

    @Constraints.Required
    @Column(name ="name")
    public String name;

    @OneToOne( cascade = CascadeType.ALL)
    public Address address;

    @Constraints.Required
    public boolean deleted;
}
