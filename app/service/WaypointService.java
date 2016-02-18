package service;

import models.Waypoint;
import org.apache.commons.collections4.CollectionUtils;
import play.Logger;
import play.db.jpa.JPA;
import repository.WaypointRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    public void setChecked(List<Long> checked) throws ServiceException {
        try {
            if(checked.size() > 0) JPA.withTransaction(() -> repository.setChecked(checked, true));
            List<Long> unchecked = getUnchecked(checked);
            if(unchecked.size() > 0)JPA.withTransaction(() -> repository.setChecked(unchecked, false));
        } catch (Throwable throwable) {
            LOGGER.error("Update employees error: {}", throwable);
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    private List<Long> getUnchecked(List<Long> ids) throws Throwable {
        List<Long> waypoints = JPA.withTransaction(() -> repository.allPoints(ids.get(0)));
        Collections.sort(waypoints);
        for(Long id: ids){
            int index = Collections.binarySearch(waypoints, id);
            if(index >= 0) {
                waypoints.remove(index);
            }
        }
        return waypoints;
    }
}
