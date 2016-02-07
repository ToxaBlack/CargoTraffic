package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import dto.PackingListDTO;
import models.PackingList;
import models.User;
import models.statuses.PackingListStatus;
import play.Logger;
import play.mvc.Controller;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import service.PackingListService;
import service.ServiceException;

import javax.inject.Inject;
import java.util.Objects;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListController.class);

    @Inject
    PackingListService service;

    @Restrict({@Group("DISPATCHER")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result addPackingList() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API add employee", oldUser.toString());
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            LOGGER.debug("Packing list: {}", json.toString());
            PackingListDTO packingListDTO = Json.fromJson(json, PackingListDTO.class);
            PackingList packingList = PackingListDTO.getPackingList(packingListDTO);
            packingList.dispatcher = oldUser;
            packingList.status = PackingListStatus.CREATED;

            try {
                service.addPackingList(packingList);
            } catch (ServiceException e) {
                LOGGER.error("error = {}", e);
                throw new ControllerException(e.getMessage(), e);
            }
        }
        return ok();
    }

}
