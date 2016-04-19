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
import service.ReportsGeneratorService;

import javax.inject.Inject;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import static play.mvc.Http.Context.Implicit.response;
import static play.mvc.Results.*;


@SubjectPresent
public class MoneyController {
    private static final Logger.ALogger LOGGER = Logger.of(MoneyController.class);

    @Inject
    MoneyService moneyService;
    @Inject
    ReportsGeneratorService reportGeneratorService;

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
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        if (Objects.isNull(financialHighlightsList))
             financialHighlightsList = new ArrayList<>();
        return ok(Json.toJson(financialHighlightsList));
    }

    @Restrict({@Group("DIRECTOR")})
    public Result getFinancialHighlightsExcel(Long minDate, Long maxDate) throws ControllerException {
        LOGGER.debug("Get excel report - minDate: {}, maxDate: {}", minDate, maxDate);
        if (maxDate < minDate) {
            LOGGER.debug("Incorrect dates");
            return badRequest("Incorrect dates");
        }
        InputStream reportInputStream;
        try {
            reportInputStream = reportGeneratorService.generateExcelReport(new Date(minDate), new Date(maxDate));
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        if (Objects.isNull(reportInputStream))
            return badRequest();
        response().setContentType("application/vnd.ms-excel");
        response().setHeader("Content-Disposition", "inline; filename=report.xls");
        return ok(reportInputStream);
    }

    @Restrict({@Group("DIRECTOR")})
    public Result getFinancialHighlightsPdf(Long minDate, Long maxDate) throws ControllerException {
        LOGGER.debug("Get pdf report - minDate: {}, maxDate: {}", minDate, maxDate);
        if (maxDate < minDate) {
            LOGGER.debug("Incorrect dates");
            return badRequest("Incorrect dates");
        }
        InputStream reportInputStream;
        try {
            reportInputStream = reportGeneratorService.generatePdfReport(new Date(minDate), new Date(maxDate));
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        if (Objects.isNull(reportInputStream))
            return badRequest();
        response().setHeader("Content-Disposition", "attachment; filename=report.pdf");
        return ok(reportInputStream);
    }

    @Restrict({@Group("DIRECTOR")})
    public Result getFinancialHighlightsCsv(Long minDate, Long maxDate) throws ControllerException {
        LOGGER.debug("Get csv report - minDate: {}, maxDate: {}", minDate, maxDate);
        if (maxDate < minDate) {
            LOGGER.debug("Incorrect dates");
            return badRequest("Incorrect dates");
        }
        InputStream reportInputStream;
        try {
            reportInputStream = reportGeneratorService.generateCsvReport(new Date(minDate), new Date(maxDate));
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e.getMessage());
            throw new ControllerException(e.getMessage(), e);
        }
        if (Objects.isNull(reportInputStream))
            return badRequest();
        response().setHeader("Content-Disposition", "attachment; filename=report.csv");
        return ok(reportInputStream);
    }
}
