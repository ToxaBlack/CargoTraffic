package models;

import models.statuses.PackingListStatus;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by dmitriy on 1.2.16.
 */

@Entity
@Table(name = "packing_list")
public class PackingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String listNumber;

    @Constraints.Required
    public Date issueDate;

    @ManyToOne
    public User dispatcher;

    @ManyToOne
    public Warehouse departureWarehouse;

    @ManyToOne
    public Warehouse destinationWarehouse;

    public PackingListStatus status;

    @OneToMany(mappedBy = "packingList", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProductInPackingList> productsInPackingList;

}
