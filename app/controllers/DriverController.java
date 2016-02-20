package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import dto.ProductDTO;
import models.LostProduct;
import models.ProductInWaybill;
import models.User;
import org.apache.commons.collections4.CollectionUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.DriverService;
import service.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DriverController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(WaybillController.class);

    @Inject
    DriverService driverService;


    @Restrict({@Group("DRIVER")})
    public Result createActOfLost() throws ControllerException {
        JsonNode json = request().body().asJson();

        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        }

        User user = (User) Http.Context.current().args.get("user");
        ArrayNode productsNode = (ArrayNode) json;
        LOGGER.debug("Create act of lost for {}", user);

        List<LostProduct> products = new ArrayList<>();
        LostProduct product;
        for (JsonNode item : productsNode) {
            try {
                ProductDTO productDTO = Json.fromJson(item, ProductDTO.class);
                product = productDTO.toLostProduct();
                product.driver = user;
            } catch(RuntimeException e) {
                LOGGER.debug("Incorrect Json format");
                return badRequest("Incorrect Json format");
            }
            products.add(product);
        }

        try {
            driverService.createActOfLost(products);
        } catch (ServiceException e) {
            LOGGER.error("error: {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }

    @Restrict({@Group("DRIVER")})
    public Result getProducts() throws ControllerException {
        List<ProductInWaybill> list;
        try {
            list = driverService.getProducts();
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok(Json.toJson(list));
    }

    @Restrict({@Group("DRIVER")})
    public Result completeDelivery() throws ControllerException {
        User user = (User) Http.Context.current().args.get("user");
        try {
            driverService.completeDelivery(user);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }
        return ok();
    }

}
