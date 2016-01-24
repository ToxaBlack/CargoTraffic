package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import models.Warehouse;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ServiceException;
import service.WarehouseService;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;


@SubjectPresent
public class WarehouseController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(WarehouseController.class);

    @Inject
    WarehouseService warehouseService;


    @Restrict({@Group("DISPATCHER")})
    public Result warehouses() throws ControllerException {
        final Map<String, String[]> stringMap = request().queryString();
        Long id = Long.parseLong(stringMap.get("id")[0]);
        Integer warehousesCount = Integer.parseInt(stringMap.get("warehouses")[0]);
        Boolean isAscOrder = Boolean.parseBoolean(stringMap.get("ascOrder")[0]);
        LOGGER.debug("id, warehouses, ascOrder: {}, {}, {}", id, warehousesCount, isAscOrder);
        LOGGER.debug("API Get warehouse list for user: {}", Http.Context.current().args.get("user").toString());

        List<Warehouse> warehouseList;
        try {
            warehouseList = warehouseService.getWarhouses(id, warehousesCount, isAscOrder);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(warehouseList));
    }
}
