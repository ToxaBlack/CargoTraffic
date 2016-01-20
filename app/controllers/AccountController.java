package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ServiceException;
import service.UserService;

import javax.inject.Inject;

/**
 * Created by Anton Chernov on 1/3/2016.
 */
@SubjectPresent
public class AccountController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(AccountController.class);

    @Inject
    UserService userService;

    public Result getAccount() throws ControllerException {
        User user = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API Return account data for user = {}", user.toString());
        return ok(Json.toJson(getAccountData(user)));
    }

    @BodyParser.Of(BodyParser.TolerantJson.class)
    public Result updateAccount() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");

        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            LOGGER.debug("API update account data for user = {}", oldUser.toString());
            try {
                userService.update(updateAccount(oldUser, json));
            } catch (ServiceException e) {
                LOGGER.error("error = {}", e);
                throw  new ControllerException(e.getMessage(), e);
            }
            return ok();
        }

    }

    private User updateAccount(User oldUser, JsonNode json) {
        oldUser.username = json.findPath("username").textValue();
        oldUser.name = json.findPath("name").textValue();
        oldUser.surname = json.findPath("surname").textValue();
        oldUser.patronymic = json.findPath("patronymic").textValue();
        oldUser.birthday = json.findPath("birthday").textValue();
        oldUser.email = json.findPath("email").textValue();
        oldUser.address.country = json.findPath("country").textValue();
        oldUser.address.city = json.findPath("city").textValue();
        oldUser.address.street = json.findPath("street").textValue();
        oldUser.address.house = json.findPath("house").textValue();
        oldUser.address.flat = json.findPath("flat").textValue();
        return oldUser;
    }

    private JsonNode getAccountData(User user) {
        ObjectNode result = Json.newObject();
        result.put("username", user.username);
        result.put("name", user.name);
        result.put("surname", user.surname);
        result.put("patronymic", user.patronymic);
        result.put("birthday", user.birthday);
        result.put("email", user.email);
        result.put("city", user.address.city);
        result.put("country", user.address.country);
        result.put("street", user.address.street);
        result.put("house", user.address.house);
        result.put("flat", user.address.flat);
        return result;
    }


}
