package dto;

import models.*;
import models.statuses.PackingListStatus;
import models.statuses.ProductStatus;

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
    public Date issueDate;
    public Warehouse departureWarehouse;
    public User dispatcher;

    public static PackingList getPackingList(PackingListDTO dto){
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
            productInPackingList.product.storageType = new StorageType(productDTO.storage.toUpperCase().trim());
            productInPackingList.product.name = productDTO.name;

            productInPackingList.count = productDTO.quantity;
            productInPackingList.price = productDTO.price;
            productInPackingList.status = ProductStatus.ACCEPTED;
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
            productDTO.price = productInPackingList.price;
            productDTO.quantity = productInPackingList.count;
            productDTO.storage = productInPackingList.product.storageType.type;
            productDTO.unit = productInPackingList.product.measureUnit.name;
            dto.products.add(productDTO);
        }
        return dto;
    }
}
