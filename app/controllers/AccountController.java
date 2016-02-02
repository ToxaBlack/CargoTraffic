package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import dto.Converter;
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
import testDTO.models.AccountDTO;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by Anton Chernov on 1/3/2016.
 */
@SubjectPresent
public class AccountController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(AccountController.class);

    @Inject
    UserService userService;

    @Inject
    Converter converter;

    public Result getAccount() throws ControllerException, IOException {
        User user = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API Return account data for user = {}", user.toString());
        return ok(Json.toJson(AccountDTO.getAccount(user)));
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

            AccountDTO account = Json.fromJson(json, AccountDTO.class);
            if (!validateAccountData(account)) {
                LOGGER.debug("Account data not valid");
                return badRequest("Account data not valid");
            }

            User user = AccountDTO.getUser(account, oldUser);

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


    private boolean validateAccountData(AccountDTO user) {
        return (validateParam(user.username, 40, true) &&
                (validateParam(user.name, 40, false)) &&
                (validateParam(user.surname, 40, true)) &&
                (validateParam(user.email, 40, true)) &&
                (validateParam(user.patronymic, 40, false)) &&
                (validateParam(user.birthday, 40, false)) &&
                (validateParam(user.country, 40, false)) &&
                (validateParam(user.city, 40, false)) &&
                (validateParam(user.street, 40, false)) &&
                (validateParam(user.house, 40, false)) &&
                (validateParam(user.flat, 40, false)));
    }

    private boolean validatePasswords(String oldPassword, String newPassword) {
        return validateParam(oldPassword, 40, true) && validateParam(newPassword, 40, true);
    }

    private boolean validateParam(String string, int maxLength, boolean checkEmpty) {
        if (checkEmpty) {
            return StringUtils.isNotBlank(string) && string.length() <= maxLength;
        } else return Objects.isNull(string) || string.length() <= maxLength;
    }


}
