package service;

import models.Waypoint;
import play.Logger;
import play.db.jpa.JPA;
import repository.WaypointRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Olga on 15.02.2016.
 */
public class WaypointService {
    private static final Logger.ALogger LOGGER = Logger.of(WaypointService.class);

    @Inject
    private WaypointRepository repository;

    public List<Waypoint> get(Long id) throws ServiceException {
        try {
            return JPA.withTransaction(() -> repository.findByWaybill(id));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }
}
