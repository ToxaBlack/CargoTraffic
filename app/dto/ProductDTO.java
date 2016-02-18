package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.MeasureUnit;
import models.Product;
import models.StorageType;

/**
 * Created by Olga on 07.02.2016.
 */
@JsonIgnoreProperties({"lastQuantity"})
public class ProductDTO {
    public Long id;
    public String name;
    public Long quantity;
    public String unit;
    public String storage;
    public Double price;

    public ProductDTO(){}

    public ProductDTO(Product product, Long quantity){
        this.id = product.id;
        this.name = product.name;
        this.unit = product.measureUnit.toString().toUpperCase();
        this.storage = product.storageType.toString().toUpperCase();
        this.quantity = quantity;
    }

    public Product toProduct(){
        Product pr = new Product();
        pr.id = id;
        pr.name = name;
        pr.measureUnit = new MeasureUnit(unit);
        pr.storageType = StorageType.valueOf(storage);
        return pr;
    }
}
