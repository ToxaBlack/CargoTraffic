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

    @Constraints.Required
    @Column(name = "issue_date")
    public Date issueDate;

    @ManyToOne
    @JoinColumn(name = "dispatcher")
    public User dispatcher;

    @ManyToOne
    @JoinColumn(name = "departure_warehouse")
    public Warehouse departureWarehouse;

    @ManyToOne
    @JoinColumn(name = "destination_warehouse")
    public Warehouse destinationWarehouse;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public PackingListStatus status;

    @OneToMany(mappedBy = "packingList", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<ProductInPackingList> productsInPackingList;

}
