package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import dto.DriverWaypointsDTO;
import exception.ServiceException;
import models.*;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;
import repository.CompanyRepository;
import repository.MoneyRepository;
import repository.WaybillRepository;
import repository.WaypointRepository;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Olga on 15.02.2016.
 */
public class WaypointService {
    private static final Logger.ALogger LOGGER = Logger.of(WaypointService.class);

    @Inject
    private WaypointRepository waypointRepository;
    @Inject
    private WaybillRepository waybillRepository;
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private MoneyRepository moneyRepository;

    private final int METERS_IN_KILOMETER = 1000;

    public List<Waypoint> get(Long id) throws ServiceException {
        try {
            return JPA.withTransaction(() -> waypointRepository.findByDriver(id));
        } catch (Throwable throwable) {
            LOGGER.error("Get list error = {}", throwable.getMessage());
            throw new ServiceException(throwable.getMessage(), throwable);
        }
    }

    public void setChecked(DriverWaypointsDTO dto) throws ServiceException {
        try {
            JPA.withTransaction(() -> {
                if (dto.checked.size() > 0)
                    waypointRepository.setChecked(dto.checked, true);
                List<Long> unchecked = getUnchecked(dto);
                if (unchecked.size() > 0)
                    waypointRepository.setChecked(unchecked, false);

                FinancialHighlights financialHighlights = new FinancialHighlights();
                User user = (User) Http.Context.current().args.get("user");
                WaybillVehicleDriver waybillVehicleDriver = waybillRepository.getWVDByDriver(user);
                financialHighlights.waybillVehicleDriver = waybillVehicleDriver;
                financialHighlights.deliveredDate = new Date();

                Long distance = calculateDistance(dto);
                Company company = companyRepository.findCompanyByName(user.company.name);
                financialHighlights.transportationIncome =
                        company.transportationCostPerKm * distance / METERS_IN_KILOMETER;

                financialHighlights.vehicleFuelLoss =
                        waybillVehicleDriver.vehicle.fuelConsumption * distance / METERS_IN_KILOMETER *
                                waybillVehicleDriver.vehicle.vehicleFuel.fuelCost;
                moneyRepository.saveFinancialHighlights(financialHighlights);
            });
        } catch (Throwable throwable) {
            LOGGER.error("Update waypoints status error: {}", throwable.getMessage());
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

    private Long calculateDistance(DriverWaypointsDTO dto) {
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
        Long totalDistance = 0L;
        for (int i = 0; i < checked.size() - 1; i++) {
            String response = sendRequest(checked.get(i), checked.get(i + 1));
            Long distance = parseResponse(response);
            totalDistance += distance;
        }
        return totalDistance;
    }

    private String sendRequest(Waypoint origin, Waypoint destination){
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
        StringBuilder answer = new StringBuilder();;
        try {
            final InputStream is = new URL(url).openStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String output;
            while ((output = br.readLine()) != null) {
                answer.append(output);
            }
        } catch (IOException e) {
            LOGGER.error("Error : {}", e.getMessage());
        }
        return answer.toString();
    }

    private String encodeParams(final Map<String, String> params) {
        StringBuilder answer = new StringBuilder();
        params.forEach((k,v)->{
            answer.append(k).append('=').append(v).append('&');
        });
        answer.deleteCharAt(answer.length() - 1);
        return answer.toString();
    }

    private Long parseResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        Long distance = 0L;
        try {
            json = mapper.readTree(response);
            distance = json.findPath("routes").findValue("legs").findPath("distance").findPath("value").asLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distance;
    }
}
