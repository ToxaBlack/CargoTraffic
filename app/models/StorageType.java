package models;

import javax.persistence.*;

/**
 * Created by Olga on 07.02.2016.
 */
@Entity
@Table(name = "storage_type")
public class StorageType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String type;

    public StorageType() {
    }

    public StorageType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
