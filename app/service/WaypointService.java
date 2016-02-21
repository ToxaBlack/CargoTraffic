package service;

import dto.DriverWaypointsDTO;
import exception.ServiceException;
import models.Waypoint;
import play.Logger;
import play.db.jpa.JPA;
import repository.WaypointRepository;

import javax.inject.Inject;
import java.util.ArrayList;
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
            LOGGER.error("Get list error = {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void setChecked(DriverWaypointsDTO dto) throws ServiceException {
        try {
            if(dto.checked.size() > 0) JPA.withTransaction(() -> repository.setChecked(dto.checked, true));
            List<Long> unchecked = getUnchecked(dto);
            if(unchecked.size() > 0)JPA.withTransaction(() -> repository.setChecked(unchecked, false));
        } catch (Throwable throwable) {
            LOGGER.error("Update employees error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    private List<Long> getUnchecked(DriverWaypointsDTO dto) throws Throwable {
        List<Long> waypoints = new ArrayList<>();
        for(Waypoint wp: dto.controlPoints){
            waypoints.add(wp.id);
        }
        Collections.sort(waypoints);
        for(Long id: dto.checked){
            int index = Collections.binarySearch(waypoints, id);
            if(index >= 0) {
                waypoints.remove(index);
            }
        }
        return waypoints;
    }
}
