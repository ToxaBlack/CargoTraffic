package service;

import models.PackingList;
import play.Logger;
import play.db.jpa.JPA;
import repository.PackingListRepository;

import javax.inject.Inject;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListService {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListService.class);
    @Inject
    private PackingListRepository repository;
    public void addPackingList(PackingList packingList) throws ServiceException {
        try {
            JPA.withTransaction(() -> repository.savePackingList(packingList));
        } catch (Throwable throwable) {
            LOGGER.error("Add packing list with id = {}", packingList.id);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
