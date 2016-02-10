package service;

import models.PackingList;
import models.Product;
import models.ProductInPackingList;
import models.User;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.PackingListRepository;
import repository.ProductRepository;

import javax.inject.Inject;
import java.util.Set;
import java.util.List;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListService {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListService.class);

    @Inject
    private PackingListRepository packingListRepository;
    @Inject
    private ProductRepository productRepository;
    public void addPackingList(PackingList packingList, Set<ProductInPackingList> products) throws ServiceException {
        try {
            JPA.withTransaction(() -> packingListRepository.savePackingList(packingList));
            for(ProductInPackingList product: products){
                Product pr = JPA.withTransaction(() -> productRepository.save(product.product));
                product.product = pr;
                product.packingList = packingList;
                product.id.productId = pr.id;
                product.id.packingListId = packingList.id;
                JPA.withTransaction(() -> packingListRepository.saveProductInPackingList(product));
            }
        } catch (Throwable throwable) {
            LOGGER.error("Add packing list with id = {}", packingList.id);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public List<PackingList> getPackingLists(Long id, Integer count, Boolean ascOrder) throws ServiceException {
        LOGGER.debug("API get packingLists: {}, {}, {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                return packingListRepository.getPackingLists(id, count, ascOrder, user.company.id);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get packingLists error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
