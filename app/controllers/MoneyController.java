package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import exception.ControllerException;
import exception.ServiceException;
import models.FinancialHighlights;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import service.MoneyService;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static play.mvc.Results.ok;


@SubjectPresent
public class MoneyController {
    private static final Logger.ALogger LOGGER = Logger.of(MoneyController.class);

    @Inject
    MoneyService moneyService;

    @Restrict({@Group("DIRECTOR")})
    public Result getFinancialHighlights(Long minDate, Long maxDate) throws ControllerException {
        LOGGER.debug("Get financial highlights - minDate: {}, maxDate: {}", minDate, maxDate);
        List<FinancialHighlights> financialHighlightsList;
        try {
            financialHighlightsList = moneyService.getFinancialHighlights(new Date(minDate), new Date(maxDate));
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(financialHighlightsList));
    }
}
