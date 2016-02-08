package service;

import models.PackingList;
import models.User;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.PackingListRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListService {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListService.class);

    @Inject
    private PackingListRepository packingListRepository;

    public void addPackingList(PackingList packingList) throws ServiceException {
        try {
            JPA.withTransaction(() -> packingListRepository.savePackingList(packingList));
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
