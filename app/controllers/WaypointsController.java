package controllers;

import be.objectify.deadbolt.java.actions.*;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import models.Waypoint;
import play.Logger;;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.WaypointsService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Olga on 13.02.2016.
 */
public class WaypointsController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(WaypointsController.class);

    @Inject
    WaypointsService service;

    @Restrict({@Group("MANAGER")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result add() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API add employee", oldUser.toString());
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            ArrayList<Waypoint> waypoints = Json.fromJson(json, ArrayList.class);
            LOGGER.debug("Waypoints: {}", json.toString());
        }
        return ok();
    }
}
