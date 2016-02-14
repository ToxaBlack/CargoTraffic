package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dto.AccountDTO;
import dto.EmployeeDTO;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.EmployeesService;
import service.ServiceException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


/**
 * Created by Olga on 25.01.2016.
 */
public class EmployeesController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(EmployeesController.class);

    @Inject
    EmployeesService employeeService;

    @Restrict({@Group("ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result addEmployees() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API add employee", oldUser.toString());
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            EmployeeDTO userDTO = Json.fromJson(json, EmployeeDTO.class);
            User user = new User();
            user = EmployeeDTO.getUser(userDTO, user);
            user.company.id = oldUser.company.id;
            //TODO Validation
                /*if (!validateAccountData(user)) {
                    LOGGER.debug("Account data not valid");
                    return badRequest("Account data not valid");
                }*/
            try {
                employeeService.addEmployee(user);
            } catch (ServiceException e) {
                LOGGER.error("error = {}", e);
                throw new ControllerException(e.getMessage(), e);
            }
        }
        return ok();
    }


    @Restrict({@Group("ADMIN")})
    public Result getEmployees(Long id, Integer employees, Boolean ascOrder) throws ControllerException {
        if (id == null || employees == null || ascOrder == null || id <= 0 || employees <= 0) {
            return badRequest("Bad parameters at getEmployees()");
        }
        Long companyId = ((User) Http.Context.current().args.get("user")).company.id;
        LOGGER.debug("company_id, id, companies, ascOrder: {}, {}, {}, {}", companyId, id, employees, ascOrder);
        List<User> companyList;
        try {
            companyList = employeeService.getEmployees(companyId, id, employees, ascOrder);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(companyList));
    }

    @Restrict({@Group("ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result removeEmployees() throws ControllerException {
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            return badRequest("Expecting Json data");
        }
        ArrayNode idsNode = (ArrayNode) json;
        Iterator<JsonNode> iterator = idsNode.elements();
        List<Long> clientIds = new ArrayList<>();
        while (iterator.hasNext()) {
            clientIds.add(iterator.next().asLong());
        }
        try {
            employeeService.removeEmployees(clientIds);
        } catch (ServiceException e) {
            LOGGER.error("error: {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }

    @Restrict({@Group("ADMIN")})
    public Result getEmployee(Long id) throws ControllerException {
        if (id == null || id <= 0) {
            return badRequest("Bad parameters at getEmployee()");
        }
        LOGGER.debug("Get employee with id {},", id);
        User user;
        try {
            user = employeeService.getEmployee(id);
            user.password = null;
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(AccountDTO.getAccount(user)));
    }

    @Restrict({@Group("ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateEmployee() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            try {
                User user = new User();
                EmployeeDTO userDTO = Json.fromJson(json, EmployeeDTO.class);;
                user = EmployeeDTO.getUser(userDTO, user);
                user.id = Long.parseLong(userDTO.id);
                user.company.id = oldUser.company.id;
                employeeService.updateEmployee(user);
            } catch (ServiceException e) {
                LOGGER.error("error = {}", e);
                throw new ControllerException(e.getMessage(), e);
            }
            return ok();
        }

    }

    @Restrict({@Group("MANAGER")})
    public Result getDrivers() throws ControllerException {
        Long companyId = ((User) Http.Context.current().args.get("user")).company.id;
        LOGGER.debug("company_id: {}", companyId);
        List<User> drivers;
        try {
            drivers = employeeService.getDrivers(companyId);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(drivers));
    }
}
