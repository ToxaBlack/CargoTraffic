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
import service.UserService;

import javax.inject.Inject;
import java.util.*;


@SubjectPresent
public class ClientsController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(ClientsController.class);

    @Inject
    CompanyService companyService;


    @Restrict({@Group("SYS_ADMIN")})
    public Result getClients() throws ControllerException {
        final Map<String, String[]> stringMap = request().queryString();
        Long id = Long.parseLong(stringMap.get("id")[0]);
        Integer clientsCount = Integer.parseInt(stringMap.get("clients")[0]);
        Boolean isAscOrder = Boolean.parseBoolean(stringMap.get("ascOrder")[0]);
        LOGGER.debug("id, clients, ascOrder: {}, {}, {}", id, clientsCount, isAscOrder);

        List<Company> companyList;
        try {
            companyList = companyService.getCompanies(id, clientsCount, isAscOrder);
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
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }
        JsonNode clientNode = json.findPath("client");
        JsonNode adminNode = json.findPath("admin");
        if (Objects.isNull(clientNode) && Objects.isNull(adminNode)) {
            LOGGER.debug("Incorrect Json format");
            return badRequest("Incorrect Json format");
        }
        Company company;
        User admin;
        try {
            company = Json.fromJson(clientNode, Company.class);
            admin = Json.fromJson(adminNode, User.class);
        } catch (RuntimeException e) {
            LOGGER.debug("Incorrect Json format");
            return badRequest("Incorrect Json format");
        }
        Company savedCompany = null;
        try {
            savedCompany = companyService.addClient(company, admin);
        } catch (ServiceException e) {
            LOGGER.error("error: {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        if (Objects.isNull(savedCompany)) {
            return status(409, "Company already exists.");
        }
        return ok(Json.toJson(savedCompany));
    }

    @Restrict({@Group("SYS_ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result lockClients() throws ControllerException {
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }
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

    @Restrict({@Group("SYS_ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result unlockClients() throws ControllerException {
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }
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
