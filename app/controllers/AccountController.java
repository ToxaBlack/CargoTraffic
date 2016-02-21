package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import dto.AccountDTO;
import dto.PasswordDTO;
import exception.ControllerException;
import exception.ServiceException;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.validation.Validation;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.UserService;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Anton Chernov on 1/3/2016.
 */
@SubjectPresent
public class AccountController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(AccountController.class);

    @Inject
    UserService userService;


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

            Set<ConstraintViolation<Object>> errors = Validation.getValidator().validate(account);
            if(!errors.isEmpty())  {
                LOGGER.debug("Account data not valid = {}",  Arrays.toString(errors.toArray()));
                return badRequest("Account data not valid");
            }

            User user = AccountDTO.getUser(account, oldUser);

            try {
                userService.update(user);
            } catch (ServiceException e) {
                LOGGER.error("error = {}", e.getMessage());
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
            PasswordDTO passwordDTO = Json.fromJson(json, PasswordDTO.class);
            Set<ConstraintViolation<Object>> errors = Validation.getValidator().validate(passwordDTO);
            if(!errors.isEmpty())  {
                LOGGER.debug("Password data not valid = {}", Arrays.toString(errors.toArray()));
                return badRequest("Password data not valid");
            }

            if (BCrypt.checkpw(passwordDTO.oldPassword, oldUser.password))
                try {
                    oldUser.password = BCrypt.hashpw(passwordDTO.newPassword, BCrypt.gensalt());
                    userService.update(oldUser);
                    return ok();
                } catch (ServiceException e) {
                    LOGGER.error("error = {}", e.getMessage());
                    throw new ControllerException(e.getMessage(), e);
                }
            else {
                LOGGER.debug("Wrong old password!");
                return badRequest("Wrong old password!");
            }
        }


    }

}
