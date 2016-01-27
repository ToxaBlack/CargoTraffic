package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Company;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.CompanyService;
import service.ServiceException;

import javax.inject.Inject;
import java.util.*;


@SubjectPresent
public class ClientsController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(ClientsController.class);

    @Inject
    CompanyService companyService;


    @Restrict({@Group("SYS_ADMIN")})
    public Result getClients(Long id, Integer clients, Boolean ascOrder ) throws ControllerException {
        LOGGER.debug("id, clients, ascOrder: {}, {}, {}", id, clients, ascOrder);

        List<Company> companyList;
        try {
            companyList = companyService.getCompanies(id, clients, ascOrder);
        } catch (ServiceException e) {
            LOGGER.error("error: {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(companyList));
    }

    @Restrict({@Group("SYS_ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result addClient() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API add client", oldUser.toString());

        JsonNode json = request().body().asJson();
        return ok();
    }

    @Restrict({@Group("SYS_ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result lockClients() throws ControllerException {
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            ArrayNode clientIdsNode = (ArrayNode) json;
            Iterator<JsonNode> iterator = clientIdsNode.elements();
            List<Long> clientIds = new ArrayList<>();
            while (iterator.hasNext()) {
                clientIds.add(iterator.next().asLong());
            }
            try {
                companyService.lockCompanies(clientIds);
            } catch (ServiceException e) {
                LOGGER.error("error: {}", e);
                throw new ControllerException(e.getMessage(), e);
            }
            return ok();
        }
    }

    @Restrict({@Group("SYS_ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result unlockClients() throws ControllerException {
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            ArrayNode clientIdsNode = (ArrayNode) json;
            Iterator<JsonNode> iterator = clientIdsNode.elements();
            List<Long> clientIds = new ArrayList<>();
            while (iterator.hasNext()) {
                clientIds.add(iterator.next().asLong());
            }
            try {
                companyService.unlockCompanies(clientIds);
            } catch (ServiceException e) {
                LOGGER.error("error: {}", e);
                throw new ControllerException(e.getMessage(), e);
            }
            return ok();
        }
    }
}
