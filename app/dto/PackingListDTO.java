package dto;

import models.*;
import models.statuses.ProductStatus;
import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListDTO {
    public Long id;
    public Warehouse destinationWarehouse;
    public List<ProductDTO> products = new ArrayList<ProductDTO>();
    @Constraints.MaxLength(40)
    public Date issueDate;
    public Warehouse departureWarehouse;
    public User dispatcher;

    public static PackingList getPackingList(PackingListDTO dto, ProductStatus status){
        PackingList packingList = new PackingList();
        packingList.departureWarehouse = dto.departureWarehouse;
        packingList.destinationWarehouse = dto.destinationWarehouse;
        packingList.issueDate = dto.issueDate;
        packingList.productsInPackingList = new HashSet<>();
        for(ProductDTO productDTO: dto.products){
            ProductInPackingList productInPackingList = new ProductInPackingList();

            productInPackingList.product = new Product();
            productInPackingList.product.deleted = false;
            productInPackingList.product.measureUnit = new MeasureUnit(productDTO.unit.toUpperCase().trim());
            productInPackingList.product.storageType = StorageType.valueOf(productDTO.storage.toUpperCase().trim());
            productInPackingList.product.name = productDTO.name;
            productInPackingList.product.price = productDTO.price;

            productInPackingList.count = productDTO.quantity;
            productInPackingList.status = status;
            productInPackingList.packingList = packingList;

            packingList.productsInPackingList.add(productInPackingList);
        }
        return packingList;
    }

    public static PackingListDTO toPackingListDTO(PackingList packingList) {
        PackingListDTO dto = new PackingListDTO();
        dto.id = packingList.id;
        dto.departureWarehouse = packingList.departureWarehouse;
        dto.destinationWarehouse = packingList.destinationWarehouse;
        dto.dispatcher = packingList.dispatcher;
        dto.issueDate = packingList.issueDate;
        for (ProductInPackingList productInPackingList : packingList.getProductsInPackingList()) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.id = productInPackingList.id.productId;
            productDTO.name = productInPackingList.product.name;
            productDTO.price = productInPackingList.product.price;
            productDTO.quantity = productInPackingList.count;
            productDTO.storage = productInPackingList.product.storageType.name();
            productDTO.unit = productInPackingList.product.measureUnit.name;
            dto.products.add(productDTO);
        }
        return dto;
    }
}
