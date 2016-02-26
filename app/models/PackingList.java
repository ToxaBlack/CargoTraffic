package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.statuses.PackingListStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmitriy on 1.2.16.
 */

@Entity
@Table(name = "packing_list")
public class PackingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "issue_date")
    public Date issueDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dispatcher")
    public User dispatcher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departure_warehouse")
    public Warehouse departureWarehouse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_warehouse")
    public Warehouse destinationWarehouse;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public PackingListStatus status;

    @OneToMany(mappedBy = "packingList")
    @JsonIgnore
    public Set<ProductInPackingList> productsInPackingList = new HashSet<>();

    public Set<ProductInPackingList> getProductsInPackingList() {
        return productsInPackingList;
    }
}
