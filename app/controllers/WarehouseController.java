package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.IOException;
import java.util.List;

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
    @BodyParser.Of(BodyParser.Json.class)
    public Result addWarehouse() throws ControllerException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = request().body().asJson();
        System.out.println(json.toString());
        Warehouse warehouse =  mapper.readValue(json.toString(), Warehouse.class);
        //String warehouseName = json.findPath("warehouseName").textValue();
        LOGGER.debug("API add warehouse with name = {}", warehouse.name);

        try {
            warehouseService.addWarehouse(warehouse);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(warehouse));
    }
}
