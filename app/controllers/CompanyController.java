package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import models.Company;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.CompanyService;
import service.ServiceException;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;


@SubjectPresent
public class CompanyController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(CompanyController.class);

    @Inject
    CompanyService companyService;


    @Restrict({@Group("SYS_ADMIN")})
    public Result companies() throws ControllerException {
        final Map<String, String[]> stringMap = request().queryString();
        Long id = Long.parseLong(stringMap.get("id")[0]);
        Integer companiesCount = Integer.parseInt(stringMap.get("companies")[0]);
        Boolean isAscOrder = Boolean.parseBoolean(stringMap.get("ascOrder")[0]);
        LOGGER.debug("id, companies, ascOrder: {}, {}, {}", id, companiesCount, isAscOrder);
        LOGGER.debug("API Get company list for user: {}", Http.Context.current().args.get("user").toString());

        List<Company> companyList;
        try {
            companyList = companyService.getCompanies(id, companiesCount, isAscOrder);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(companyList));
    }
}
