package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Anton Chernov on 12/30/2015.
 */

@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Constraints.Required
    public long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public Date date;

    @Constraints.Required
    @Column(name = "transportation_cost_per_km")
    public Double transportationCostPerKm;

    @Constraints.Required
    public Boolean locked;
}
