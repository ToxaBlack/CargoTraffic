package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import dto.WaybillDTO;
import exception.ControllerException;
import models.*;
import models.statuses.PackingListStatus;
import models.statuses.WaybillStatus;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.PackingListService;
import exception.ServiceException;
import service.WaybillService;

import java.util.List;
import java.util.Objects;

/**
 * Created by Olga on 14.02.2016.
 */
public class WaybillController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(WaybillController.class);

    @Inject
    WaybillService service;

    @Inject
    PackingListService packingListService;


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
            LOGGER.debug("Waybill: {}", json.toString());
            WaybillDTO waybillDTO = Json.fromJson(json, WaybillDTO.class);
            Waybill waybill;
            try {
                waybill = waybillDTO.toWaybill();
                waybill.status = WaybillStatus.TRANSPORTATION_STARTED;
                for(WaybillVehicleDriver wvd : waybill.vehicleDrivers)
                    wvd.status = WaybillStatus.TRANSPORTATION_STARTED;
                service.addWaybill(waybill);
            } catch (ServiceException e) {
                LOGGER.error("error = {}", e.getMessage());
                throw new ControllerException(e.getMessage(), e);
            }
        }
        return ok();
    }

    @Restrict({@Group("MANAGER")})
    public Result getWaybillWithAdditionalInfoEdit(long id) throws ControllerException {
        //TODO: finish getting of waybill
        User user = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API get waybill: {}, {}", user.toString(), id);
        Waybill waybill;
        List<User> drivers;
        List<Vehicle> vehicles;
        try {
            waybill = service.getWaybill(id);
            drivers = service.getDrivers(user.company.id);
            vehicles = service.getVehicles();
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        WaybillDTO dto = WaybillDTO.toWaybillDTO(user,waybill,drivers,vehicles);
        return ok(Json.toJson(dto));
    }

    @Restrict({@Group("MANAGER")})
    public Result getAdditionalInfoForWaybill(long id) throws ControllerException {
        User user = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API get waybill: {}, {}", user.toString(), id);
        PackingList packingList;
        List<User> drivers;
        List<Vehicle> vehicles;
        try {
            packingList = packingListService.getPackingList(id, PackingListStatus.CHECKED);
            drivers = service.getDrivers(user.company.id);
            vehicles = service.getVehicles();
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        WaybillDTO dto = WaybillDTO.toWaybillDTO(user,packingList,drivers,vehicles);
        return ok(Json.toJson(dto));
    }

}
