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
    public Warehouse departureWarehouse;
    public Warehouse destinationWarehouse;
    public List<ProductDTO> products = new ArrayList<ProductDTO>();
    public Date issueDate;

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
}
