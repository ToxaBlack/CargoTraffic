package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Olga on 07.02.2016.
 */

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Column(name = "price", nullable = false, insertable = true, updatable = true)
    public Double price;

    public Boolean deleted;
}
