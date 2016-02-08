package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.User;
import models.Vehicle;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import service.ServiceException;
import service.VehicleService;

import javax.inject.Inject;
import java.util.*;

import static play.mvc.Controller.request;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;


/**
 * Created by dmitriy on 2.2.16.
 */

@SubjectPresent
public class VehiclesController {
    private static final Logger.ALogger LOGGER = Logger.of(VehiclesController.class);

    @Inject
    VehicleService vehicleService;

    @Restrict({@Group("ADMIN"),@Group("MANAGER")})
    public Result getVehicles(Long id, Integer count, Boolean ascOrder) throws ControllerException {
        LOGGER.debug("Get vehicles id, count, ascOrder: {}, {}, {}", id, count, ascOrder);
        List<Vehicle> vehicleList;
        try {
            vehicleList = vehicleService.getVehicles(id, count, ascOrder);
        } catch (ServiceException e) {
            LOGGER.error("error: {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(vehicleList));
    }

    @Restrict({@Group("ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result addVehicle() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API add vehicle", oldUser.toString());
        JsonNode vehicleNode = request().body().asJson();
        if (Objects.isNull(vehicleNode)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }
        Vehicle vehicle;
        try {
            vehicle = Json.fromJson(vehicleNode, Vehicle.class);
        } catch (RuntimeException e) {
            LOGGER.debug("Incorrect Json format");
            return badRequest("Incorrect Json format");
        }
        Vehicle savedVehicle;
        try {
            savedVehicle = vehicleService.addVehicle(vehicle);
        } catch (ServiceException e) {
            LOGGER.error("error: {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(savedVehicle));
    }

    @Restrict({@Group("ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateVehicle() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API update vehicle", oldUser.toString());
        JsonNode vehicleNode = request().body().asJson();
        if (Objects.isNull(vehicleNode)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }
        Vehicle vehicle;
        try {
            vehicle = Json.fromJson(vehicleNode, Vehicle.class);
        } catch (RuntimeException e) {
            LOGGER.debug("Incorrect Json format");
            return badRequest("Incorrect Json format");
        }
        try {
            vehicleService.updateVehicle(vehicle);
        } catch (ServiceException e) {
            LOGGER.error("error: {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(vehicle));
    }

    @Restrict({@Group("ADMIN")})
    public Result deleteVehicles() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API delete vehicle", oldUser.toString());
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }
        ArrayNode vehicleIdsNode = (ArrayNode) json;
        Iterator<JsonNode> iterator = vehicleIdsNode.elements();
        List<Long> vehicleIds = new ArrayList<>();
        while (iterator.hasNext()) {
            vehicleIds.add(iterator.next().asLong());
        }
        LOGGER.debug("Delete vehicles id: {}", Arrays.toString(vehicleIds.toArray()));
        try {
            vehicleService.deleteVehicles(vehicleIds);
        } catch (ServiceException e) {
            LOGGER.error("error: {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }
}
