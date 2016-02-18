package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import models.Waypoint;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ServiceException;
import service.WaypointService;

import javax.inject.Inject;
import java.util.ArrayList;
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
    public Result get(Long id) throws ControllerException {
        User user =  (User) Http.Context.current().args.get("user");
        LOGGER.debug("API update account data for user = {}",user.toString());
        List<Waypoint> points;
        try {
            points = service.get(id);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
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
        ArrayList<String> checked = Json.fromJson(json, ArrayList.class);
        ArrayList<Long> ids = new ArrayList<>();
        for(String id: checked){
            ids.add(Long.parseLong(id));
        }
        try {
            service.setChecked(ids);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }

}
