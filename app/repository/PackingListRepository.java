package repository;

import models.PackingList;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListRepository {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListRepository.class);

    public PackingList savePackingList(PackingList packingList) {
        EntityManager em = JPA.em();

        return packingList;
    }
}
