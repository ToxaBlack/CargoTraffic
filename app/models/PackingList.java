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
    public String listNumber;

    @Constraints.Required
    public Date issueDate;

    @ManyToOne
    public User dispatcher;

    @Constraints.Required
    public PackingListStatus status;


    @OneToMany(mappedBy = "packingList", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProductInPackingList> productsInPackingList;

    /*@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="product_in_packing_list",
            joinColumns =
                    {@JoinColumn(name="packing_list_id")},
            inverseJoinColumns =
                    {@JoinColumn(name="product_id")})
    public Set<Product> products;

*/
}
