package models;

import models.statuses.ProductStatus;

import javax.annotation.concurrent.Immutable;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Olga on 07.02.2016.
 */
@Entity
@Table(name = "product_in_packing_list")
@Immutable
public class ProductInPackingList {

    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "product_id")
        public Long productId;

        @Column(name = "packing_list_id")
        public Long packingListId;

        public Id() {
        }

        public Id(Long productID, Long packingListId) {
            this.productId = productID;
            this.packingListId = packingListId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.packingListId.equals(that.packingListId)
                        && this.productId.equals(that.productId);
            }
            return false;
        }
        public int hashCode() {
            return packingListId.hashCode() + productId.hashCode();
        }
    }

    @EmbeddedId
    public Id id = new Id();



    @Column(name = "count", nullable = false, insertable = true, updatable = true)
    public Long count;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public ProductStatus status;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            insertable = false, updatable = false)
    public Product product;

    @ManyToOne
    @JoinColumn(
            name = "packing_list_id",
            insertable = false, updatable = false)
    public PackingList packingList;
}
