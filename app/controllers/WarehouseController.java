package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import models.Warehouse;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.collection.JavaConversions$;
import service.ServiceException;
import service.WarehouseService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SubjectPresent
public class WarehouseController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(WarehouseController.class);

    @Inject
    WarehouseService warehouseService;

    @Restrict({@Group("DISPATCHER")})
    public Result getWarehouses(Long id, Integer warehouses, Boolean ascOrder) throws ControllerException {
        System.out.println("count:"+warehouses);
        LOGGER.debug("API Get warehouse list for user: {}; id, warehouses, ascOrder: {}, {}, {}",
                Http.Context.current().args.get("user").toString(), id, warehouses, ascOrder);

        List<Warehouse> warehouseList;
        try {
            warehouseList = warehouseService.getWarhouses(id, warehouses, ascOrder);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(warehouseList));
    }

    @Restrict({@Group("DISPATCHER")})
    public Result addWarehouse() throws ControllerException {
        JsonNode json = request().body().asJson();
        Warehouse warehouse =  Json.fromJson(json, Warehouse.class);
        LOGGER.debug("API add warehouse with name = {}", warehouse.name);
        try {
           warehouse = warehouseService.addWarehouse(warehouse);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(warehouse));
    }

    @Restrict({@Group("DISPATCHER")})
    public Result editWarehouse() throws ControllerException {
        JsonNode json = request().body().asJson();
        Warehouse warehouse =  Json.fromJson(json, Warehouse.class);
        LOGGER.debug("API edit warehouse with name = {}", warehouse.name);
        try {
           warehouse = warehouseService.editWarehouse(warehouse);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(warehouse));
    }

    @Restrict({@Group("DISPATCHER")})
    public Result removeWarehouse() throws ControllerException {
        JsonNode json = request().body().asJson();
        List<Warehouse> warehouses = new ArrayList<>();
        for (JsonNode item : json.withArray("array")) {
            warehouses.add(Json.fromJson(item, Warehouse.class));
        }
        warehouseService.removeWarehouses(warehouses);
        return ok();
    }
}
