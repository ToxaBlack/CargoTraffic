package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.User;
import models.Waypoint;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ServiceException;
import service.WaypointService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
        List<Waypoint> warehouseList;
        try {
            warehouseList = service.get(id);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(warehouseList));
    }
}
