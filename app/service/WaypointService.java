package service;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import dto.DriverWaypointsDTO;
import exception.ServiceException;
import models.Waypoint;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.Json;
import repository.WaypointRepository;
import scala.util.parsing.json.JSONObject;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Olga on 15.02.2016.
 */
public class WaypointService {
    private static final Logger.ALogger LOGGER = Logger.of(WaypointService.class);

    @Inject
    private WaypointRepository repository;

    public List<Waypoint> get(Long id) throws ServiceException {
        try {
            return JPA.withTransaction(() -> repository.findByDriver(id));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void setChecked(DriverWaypointsDTO dto) throws ServiceException {
        try {
            if (dto.checked.size() > 0) JPA.withTransaction(() -> repository.setChecked(dto.checked, true));
            List<Long> unchecked = getUnchecked(dto);
            if (unchecked.size() > 0) JPA.withTransaction(() -> repository.setChecked(unchecked, false));
            calculateDistance(dto);
        } catch (Throwable throwable) {
            LOGGER.error("Update employees error: {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    private List<Long> getUnchecked(DriverWaypointsDTO dto) throws Throwable {
        List<Long> waypoints = new ArrayList<>();
        for (Waypoint wp : dto.controlPoints) {
            waypoints.add(wp.id);
        }
        Collections.sort(waypoints);
        for (Long id : dto.checked) {
            int index = Collections.binarySearch(waypoints, id);
            if (index >= 0) {
                waypoints.remove(index);
            }
        }
        return waypoints;
    }

    private void calculateDistance(DriverWaypointsDTO dto){
        List<Long> waypoints = new ArrayList<>();
        for (Waypoint wp : dto.controlPoints) {
            waypoints.add(wp.id);
        }
        Collections.sort(waypoints);
        List<Waypoint> checked = new ArrayList<>();
        for (Long id : dto.checked) {
            int index = Collections.binarySearch(waypoints, id);
            if (index >= 0) {
                checked.add(dto.controlPoints.get(index));
            }
        }
        sendRequest(checked.get(0), checked.get(1));
    }

    private void sendRequest(Waypoint origin, Waypoint destination){
        final String baseUrl = "http://maps.googleapis.com/maps/api/directions/json";
        final Map<String, String> params = Maps.newHashMap();
        params.put("sensor", "false");
        params.put("language", "en");
        params.put("mode", "driving");
        params.put("origin", new StringBuilder(String.valueOf(origin.lat)).append(",")
                .append(String.valueOf(origin.lng)).toString());
        params.put("destination", new StringBuilder(String.valueOf(destination.lat)).append(",")
                .append(String.valueOf(destination.lng)).toString());
        final String url = baseUrl + '?' + encodeParams(params);
        try {
            final InputStream is = new URL(url).openStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String output;
            StringBuilder answer = new StringBuilder();
            while ((output = br.readLine()) != null) {
                answer.append(output);
            }
            LOGGER.debug(answer.toString());
        } catch (IOException e) {
            LOGGER.error("Error : {}", e.getMessage());
        }
    }
    private String encodeParams(final Map<String, String> params) {
        StringBuilder answer = new StringBuilder();
        params.forEach((k,v)->{
            answer.append(k).append('=').append(v).append('&');
        });
        answer.deleteCharAt(answer.length() - 1);
        return answer.toString();
    }

}
