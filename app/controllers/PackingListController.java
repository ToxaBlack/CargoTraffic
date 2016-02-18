package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import dto.PackingListDTO;
import models.PackingList;
import models.ProductInPackingList;
import models.User;
import models.UserRole;
import models.statuses.PackingListStatus;
import models.statuses.ProductStatus;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.PackingListService;
import service.ServiceException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        LOGGER.debug("API add packing list", oldUser.toString());
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            LOGGER.debug("Packing list: {}", json.toString());
            PackingListDTO packingListDTO = Json.fromJson(json, PackingListDTO.class);
            PackingList packingList = PackingListDTO.getPackingList(packingListDTO, ProductStatus.ACCEPTED);
            packingList.dispatcher = oldUser;
            packingList.status = PackingListStatus.CREATED;
            Set<ProductInPackingList> products = packingList.productsInPackingList;
            packingList.productsInPackingList = null;
            try {
                service.addPackingList(packingList, products);
            } catch (ServiceException e) {
                LOGGER.error("error = {}", e);
                throw new ControllerException(e.getMessage(), e);
            }
        }
        return ok();
    }


    @Restrict({@Group("MANAGER"), @Group("DISPATCHER")})
    public Result getPackingLists(Long id, Integer count, Boolean ascOrder) throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        String userRole = "";

        //Manager has more privileges
        for(UserRole role: oldUser.userRoleList) {
            if("MANAGER".equals(role.getName())) {
                userRole = role.getName();
                break;
            }
            if("DISPATCHER".equals(role.getName())) userRole = role.getName();
        }
        LOGGER.debug("API get packingLists: {}, {}, {}, {}", oldUser.toString(), id, count, ascOrder);
        List<PackingList> packingLists;
        try {
            switch(userRole) {
                case "MANAGER":
                    packingLists = service.getPackingLists(id, count, ascOrder);
                    break;
                case "DISPATCHER":
                    packingLists = service.getDispatcherPackingLists(id,count,ascOrder);
                    break;
                default:
                    packingLists = new ArrayList<>();
            }
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        List<PackingListDTO> packingListDTOs = new ArrayList<>();
        for (PackingList packingList : packingLists) {
            packingListDTOs.add(PackingListDTO.toPackingListDTO(packingList));
        }
        return ok(Json.toJson(packingListDTOs));
    }

    @Restrict({@Group("MANAGER")})
    public Result getCheckedPackingList(Long id) throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API get packingList: {}, {}", oldUser.toString(), id);
        PackingList packingList;
        try {
            packingList = service.getPackingList(id, PackingListStatus.CHECKED);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        PackingListDTO dto = PackingListDTO.toPackingListDTO(packingList);
        return ok(Json.toJson(dto));
    }

    @Restrict({@Group("MANAGER")})
    public Result getCreatedPackingList(Long id) throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API get packingList: {}, {}", oldUser.toString(), id);
        PackingList packingList;
        try {
            packingList = service.getPackingList(id, PackingListStatus.CREATED);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        PackingListDTO dto = PackingListDTO.toPackingListDTO(packingList);
        return ok(Json.toJson(dto));
    }

    @Restrict({@Group("MANAGER")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result changeStatus(Long id) throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API change status: {}, {}", oldUser.toString(), id);
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }
        String statusName = json.findPath("status").asText();
        PackingListStatus packingListStatus = PackingListStatus.valueOf(statusName);
        try {
            service.changeStatus(id, packingListStatus);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }
}
