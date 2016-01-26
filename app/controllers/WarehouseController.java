package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import models.Warehouse;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
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

    @Restrict({@Group("DISPATCHER")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result addWarehouse() throws ControllerException {
        JsonNode json = request().body().asJson();
        String warehouseName = json.findPath("warehouseName").textValue();
        LOGGER.debug("API add warehouse with name = {}", warehouseName);
        Warehouse warehouse = new Warehouse();
        warehouse.name = warehouseName;

        try {
            warehouseService.addWarehouse(warehouse);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }
}
