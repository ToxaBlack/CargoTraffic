package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.List;
import java.util.Map;


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
            LOGGER.error("error = {}", e);
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
}
