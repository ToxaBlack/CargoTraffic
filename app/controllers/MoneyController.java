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
import java.util.Objects;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;


@SubjectPresent
public class MoneyController {
    private static final Logger.ALogger LOGGER = Logger.of(MoneyController.class);

    @Inject
    MoneyService moneyService;

    @Restrict({@Group("DIRECTOR")})
    public Result getFinancialHighlights(Long minDate, Long maxDate) throws ControllerException {
        LOGGER.debug("Get financial highlights - minDate: {}, maxDate: {}", minDate, maxDate);
        if (maxDate < minDate) {
            LOGGER.debug("Incorrect dates");
            return badRequest("Incorrect dates");
        }
        List<FinancialHighlights> financialHighlightsList;
        try {
            financialHighlightsList = moneyService.getFinancialHighlights(new Date(minDate), new Date(maxDate));
            LOGGER.debug("TEST: {}, {}", financialHighlightsList.size(), financialHighlightsList.get(0));
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(financialHighlightsList));
    }
}
