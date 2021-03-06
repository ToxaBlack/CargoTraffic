package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import dto.DriverWaypointsDTO;
import exception.ControllerException;
import models.User;
import models.Waypoint;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import exception.ServiceException;
import service.WaypointService;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * Created by Olga on 15.02.2016.
 */
public class WaypointController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(WaypointController.class);

    @Inject
    WaypointService service;

    @Restrict({@Group("DRIVER")})
    public Result get() throws ControllerException {
        User user =  (User) Http.Context.current().args.get("user");
        LOGGER.debug("API update account data for user = {}",user.toString());
        List<Waypoint> points;
        try {
            points = service.get(user.id);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(points));
    }

    @Restrict({@Group("DRIVER")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result set() throws ControllerException {
        User user =  (User) Http.Context.current().args.get("user");
        LOGGER.debug("API update account data for user = {}",user.toString());
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }
        LOGGER.debug("Driver waypoints = {} ", json);
        DriverWaypointsDTO dto = Json.fromJson(json, DriverWaypointsDTO.class);
        try {
            service.setChecked(dto);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }

}
