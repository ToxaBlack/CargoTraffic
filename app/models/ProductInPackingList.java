package models;

import models.statuses.ProductStatus;

import javax.persistence.*;

/**
 * Created by Olga on 07.02.2016.
 */
@Entity
@Table(name = "product_in_packing_list")
public class ProductInPackingList {
    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packing_list_id", nullable = false)
    public PackingList packingList;

    @Column(name = "price", nullable = false, insertable = true, updatable = true)
    public Long price;

    @Column(name = "count", nullable = false, insertable = true, updatable = true)
    public Long count;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public ProductStatus status;

    public Boolean deleted;
}
