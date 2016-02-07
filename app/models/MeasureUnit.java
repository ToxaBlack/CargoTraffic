package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Olga on 07.02.2016.
 */
@Entity
@Table(name = "measure_unit")
public class MeasureUnit {
    @Id
    public Long id;
    public String name;

    public MeasureUnit(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
