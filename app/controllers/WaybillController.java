package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import models.ProductInWaybill;
import com.google.inject.Inject;
import dto.WaybillDTO;
import models.User;
import models.Waybill;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ServiceException;
import service.WaybillService;
import play.libs.Json;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Olga on 14.02.2016.
 */
public class WaybillController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(WaybillController.class);

    @Inject
    WaybillService service;


    @Restrict({@Group("MANAGER")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result add() throws ControllerException {
        User user = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API add waybill, manager {}", user.toString());
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            LOGGER.debug("Waybill with waypoints: {}", json.toString());
        }
        return ok();
    }

    @Restrict({@Group("DRIVER")})
    public Result getProducts() throws ControllerException {
        List<ProductInWaybill> list;
        try {
            list = service.getProducts();
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        Iterator<ProductInWaybill> iterator = list.iterator();
        while(iterator.hasNext()){
            ProductInWaybill product = iterator.next();
            product.waybillVehicleDriver = null;
        }
        return ok(Json.toJson(list));
    }


    @Restrict({@Group("MANAGER")})
    public Result getWaybillWithAdditionalInfo(long id) throws ControllerException {
        User user = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API get waybill: {}, {}", user.toString(), id);
        Waybill waybill;
        try {
            waybill = service.getWaybill(id);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        WaybillDTO dto = WaybillDTO.toWaybillDTO(waybill);
        return ok(Json.toJson(dto));
    }

}
