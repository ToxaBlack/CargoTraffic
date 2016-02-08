package service;

import models.PackingList;
import models.Product;
import models.ProductInPackingList;
import play.Logger;
import play.db.jpa.JPA;
import repository.PackingListRepository;
import repository.ProductRepository;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListService {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListService.class);
    @Inject
    private PackingListRepository pLRepository;
    @Inject
    private ProductRepository productRepository;
    public void addPackingList(PackingList packingList, Set<ProductInPackingList> products) throws ServiceException {
        try {
            JPA.withTransaction(() -> pLRepository.savePackingList(packingList));
            for(ProductInPackingList product: products){
                Product pr = JPA.withTransaction(() -> productRepository.save(product.product));
                product.product = pr;
                product.packingList = packingList;
                product.id.productId = pr.id;
                product.id.packingListId = packingList.id;
                JPA.withTransaction(() -> pLRepository.saveProductInPackingList(product));
            }
        } catch (Throwable throwable) {
            LOGGER.error("Add packing list with id = {}", packingList.id);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
