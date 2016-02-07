package models;

import models.statuses.ProductStatus;

import javax.persistence.*;

/**
 * Created by Olga on 07.02.2016.
 */
@Entity
@Table(name = "product_in_packing_list")
public class ProductInPackingList {

    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "product_id", nullable = false)
    public Product product;

    @ManyToOne
    @Column(name = "packing_list_id", nullable = false)
    public PackingList packingList;

    public Long price;
    public Long count;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public ProductStatus status;

    public Boolean deleted;
}
