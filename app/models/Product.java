package models;

import javax.persistence.*;

/**
 * Created by Olga on 07.02.2016.
 */

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;

    @ManyToOne
    @Column(name = "measure_unit_id")
    public MeasureUnit measureUnit;

    @ManyToOne
    @Column(name = "storage_type_id")
    public StorageType storageType;

    public Boolean deleted;

}
