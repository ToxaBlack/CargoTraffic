package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import dto.Converter;
import dto.UserDTO;
import models.User;
import models.UserRole;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.CompanyEmployeesService;
import service.ServiceException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Olga on 25.01.2016.
 */
public class CompanyEmployeesController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyEmployeesController.class);

    @Inject
    CompanyEmployeesService companyService;

    @Restrict({@Group("ADMIN")})
    @BodyParser.Of(BodyParser.Json.class)
    public Result addEmployees() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API add employee", oldUser.toString());
        JsonNode json = request().body().asJson();
        //LOGGER.debug("Our employee: {}", json.toString());
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            UserDTO userDTO = new UserDTO();
            Converter converter = new Converter();
            try {
                userDTO = converter.convertJsonToEmployeeDTO(json.toString(), userDTO);
                User user = new User();
                user = converter.convertUserDTOToUser(userDTO, user);
                user.company.id = oldUser.company.id;
                /*if (!validateAccountData(user)) {
                    LOGGER.debug("Account data not valid");
                    return badRequest("Account data not valid");
                }*/
                try {
                    companyService.addEmployee(user);
                } catch (ServiceException e) {
                    LOGGER.error("error = {}", e);
                    throw new ControllerException(e.getMessage(), e);
                }
            } catch (IOException e) {
                LOGGER.debug("Error converting json to UserDTO ");
                return badRequest("Bad account data");
            }
        }
        return ok();
    }


    @Restrict({@Group("ADMIN")})
    public Result getEmployees(Long id, Integer employees, Boolean ascOrder) throws ControllerException {
        if(id == null || employees == null || ascOrder == null || id <= 0 || employees <= 0) {
            return badRequest("Bad parameters at getEmployees()");
        }
        Long companyId = ((User) Http.Context.current().args.get("user")).company.id;
        LOGGER.debug("company_id, id, companies, ascOrder: {}, {}, {}, {}", companyId, id, employees, ascOrder);
        List<User> companyList;
        try {
            companyList = companyService.getCompanyEmployees(companyId, id, employees, ascOrder);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(companyList));
    }

    /*private UserDTO fillEmployee(UserDTO user, String json){
        Converter converter = new Converter();
        try {
            user = converter.convertJsonToEmployeeDTO(json, user);
        } catch (IOException e) {
            LOGGER.error("error = {}", e);
        }
        return user;
    }*/
    private boolean validateAccountData(User user) {
        return (validateParam(user.username, 40, true) &&
                (validateParam(user.name, 40, false)) &&
                (validateParam(user.surname, 40, true)) &&
                (validateParam(user.email, 40, true)) &&
                (validateParam(user.patronymic, 40, false)) &&
                (validateParam(user.birthday, 40, false)) &&
                (validateParam(user.address.country, 40, false)) &&
                (validateParam(user.address.city, 40, false)) &&
                (validateParam(user.address.street, 40, false)) &&
                (validateParam(user.address.house, 40, false)) &&
                (validateParam(user.address.flat, 40, false)));
    }
    private boolean validateParam(String string, int maxLength, boolean checkEmpty) {
        if (checkEmpty) {
            if (StringUtils.isNotBlank(string) && string.length() <= maxLength) return true;
        } else if (!Objects.isNull(string) && string.length() <= maxLength) return true;
        return false;
    }
}
