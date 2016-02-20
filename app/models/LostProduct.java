package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "lost_product")
public class LostProduct implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            updatable = false)
    public Product product;

    @Id
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",
            referencedColumnName = "id")
    public User driver;

    @Column(name = "quantity")
    public Long quantity;

    @Override
    public int hashCode() {
        int result = 31 * (product != null ? product.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ProductInWaybill that = (ProductInWaybill) obj;

        return (product != null ? product.equals(that.product) : that.product == null)
                && (driver != null ? driver.equals(that.waybillVehicleDriver) : that.waybillVehicleDriver == null)
                && (quantity != null ? quantity.equals(that.quantity) : that.quantity == null);
    }

    public LostProduct() {
    }

    public LostProduct(Product product, User driver, Long quantity) {
        this.product = product;
        this.driver = driver;
        this.quantity = quantity;
    }
}
