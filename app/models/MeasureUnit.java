package models;

import javax.persistence.*;

/**
 * Created by Olga on 07.02.2016.
 */
@Entity
@Table(name = "measure_unit")
public class MeasureUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;

    public MeasureUnit() {
    }

    public MeasureUnit(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
