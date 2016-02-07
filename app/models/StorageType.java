package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Olga on 07.02.2016.
 */
@Entity
@Table(name = "storage_type")
public class StorageType {
    @Id
    public Long id;
    public String type;

    @Override
    public String toString() {
        return type;
    }
}
