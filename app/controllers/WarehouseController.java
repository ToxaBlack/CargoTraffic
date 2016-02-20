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
import service.ServiceException;
import service.WarehouseService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SubjectPresent
public class WarehouseController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(WarehouseController.class);

    @Inject
    WarehouseService warehouseService;

    @Restrict({@Group("DISPATCHER")})
    public Result getWarehouses(Long id, Integer warehouses, Boolean ascOrder) throws ControllerException {
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
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }

        Warehouse warehouse;
        try {
            warehouse = Json.fromJson(json, Warehouse.class);
        } catch(RuntimeException e) {
            LOGGER.debug("Incorrect Json format");
            return badRequest("Incorrect Json format");
        }

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

        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }

        Warehouse warehouse;
        try {
            warehouse = Json.fromJson(json, Warehouse.class);
        } catch(RuntimeException e) {
            LOGGER.debug("Incorrect Json format");
            return badRequest("Incorrect Json format");
        }

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

        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }

        LOGGER.debug("API delete warehouses");

        List<Warehouse> warehouses = new ArrayList<>();
        Warehouse warehouse;
        for (JsonNode item : json.withArray("array")) {
            try {
                warehouse = Json.fromJson(item, Warehouse.class);
            } catch(RuntimeException e) {
                LOGGER.debug("Incorrect Json format");
                return badRequest("Incorrect Json format");
            }
            warehouses.add(warehouse);
        }
        try {
            warehouseService.removeWarehouses(warehouses);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }
}
