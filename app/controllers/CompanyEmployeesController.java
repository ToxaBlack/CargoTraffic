package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.CompanyEmployeesService;
import service.ServiceException;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;



/**
 * Created by Olga on 25.01.2016.
 */
public class CompanyEmployeesController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyEmployeesController.class);

    @Inject
    CompanyEmployeesService companyService;


    @Restrict({@Group("SYS_ADMIN")})
    public Result getEmployees(Long id, Integer employees, Boolean ascOrder) throws ControllerException {
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
}
