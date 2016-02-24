package service;

import exception.ServiceException;
import models.PackingList;
import models.Product;
import models.ProductInPackingList;
import models.User;
import models.statuses.PackingListStatus;
import models.statuses.ProductStatus;
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

    public List<PackingList> getPackingLists(Long id, Integer count, Boolean ascOrder, Boolean isNew) throws ServiceException {
        LOGGER.debug("API get packingLists: id {}, count {}, asc {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                return packingListRepository.getPackingLists(id, count, ascOrder, user.company.id, isNew);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get packingLists error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public List<PackingList> getDispatcherPackingLists(Long id, Integer count, Boolean ascOrder) throws ServiceException {
        LOGGER.debug("API get packingLists for dispatcher: id {}, count {}, asc {}", id, count, ascOrder);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                return packingListRepository.getDispatcherPackingLists(id, count, ascOrder,user);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get packingLists for dispatcher error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }


    public PackingList getPackingList(Long id, PackingListStatus status) throws ServiceException {
        LOGGER.debug("API get packingList: {}", id);
        try {
            return JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                return packingListRepository.getPackingList(id, user.company.id, status);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Get packingList error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void changeStatus(Long id, PackingListStatus packingListStatus) throws ServiceException {
        LOGGER.debug("API change status: {}", id);
        try {
            JPA.withTransaction(() -> {
                User user = (User) Http.Context.current().args.get("user");
                PackingList packingList = packingListRepository.getPackingList(id, user.company.id, PackingListStatus.CREATED);
                if (packingListStatus == PackingListStatus.CHECKED) {
                    packingList.status = PackingListStatus.CHECKED;
                    for (ProductInPackingList productInPackingList : packingList.getProductsInPackingList()) {
                        productInPackingList.status = ProductStatus.VERIFICATION_COMPLETED;
                    }
                } else {
                    packingList.status = PackingListStatus.REJECTED;
                }
                packingListRepository.savePackingList(packingList);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Change status packingList error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
