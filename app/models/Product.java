package models;

import javax.persistence.*;

/**
 * Created by Olga on 07.02.2016.
 */

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "measure_unit_id")
    public MeasureUnit measureUnit;

    @Column(name = "storage_type")
    @Enumerated(EnumType.STRING)
    public StorageType storageType;

    public Boolean deleted;

}
