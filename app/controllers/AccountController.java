package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.User;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ServiceException;
import service.UserService;

import javax.inject.Inject;
import java.util.Objects;

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

    @BodyParser.Of(BodyParser.Json.class)
    public Result updateAccount() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API update account data for user = {}", oldUser.toString());

        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            User user = updateAccount(oldUser, json);
            if (!validateAccountData(user)) {
                LOGGER.debug("Account data not valid");
                return badRequest("Account data not valid");
            }
            try {
                userService.update(user);
            } catch (ServiceException e) {
                LOGGER.error("error = {}", e);
                throw new ControllerException(e.getMessage(), e);
            }
            return ok();
        }

    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result updatePassword() throws ControllerException {
        User oldUser = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API update password  for user = {}", oldUser.toString());

        JsonNode json = request().body().asJson();
        if (Objects.isNull(json)) {
            LOGGER.debug("Expecting Json data");
            return badRequest("Expecting Json data");
        } else {
            String oldPassword = json.findPath("oldPassword").textValue();
            String newPassword = json.findPath("newPassword").textValue();
            if (!validatePasswords(oldPassword, newPassword)) {
                LOGGER.debug("Password data not valid");
                return badRequest("Password data not valid");
            }

            if (BCrypt.checkpw(oldPassword, oldUser.password))
                try {
                    oldUser.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                    userService.update(oldUser);
                    return ok();
                } catch (ServiceException e) {
                    LOGGER.error("error = {}", e);
                    throw new ControllerException(e.getMessage(), e);
                }
            else {
                LOGGER.debug("Wrong old password!");
                return badRequest("Wrong old password!");
            }
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

    private boolean validatePasswords(String oldPassword, String newPassword) {
        return validateParam(oldPassword, 40, true) && validateParam(newPassword, 40, true);
    }

    private boolean validateParam(String string, int maxLength, boolean checkEmpty) {
        if (checkEmpty) {
            if (StringUtils.isNotBlank(string) && string.length() <= maxLength) return true;
        } else if (!Objects.isNull(string) && string.length() <= maxLength) return true;
        return false;
    }


}
