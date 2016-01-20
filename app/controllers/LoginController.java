package controllers;

import be.objectify.deadbolt.java.actions.SubjectNotPresent;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import security.TokenController;
import service.ServiceException;
import service.UserService;

import javax.inject.Inject;

/**
 * Created by Anton Chernov on 12/30/2015.
 */

public class LoginController extends Controller {
    private static final Logger.ALogger LOGGER = Logger.of(LoginController.class);

    @Inject
    UserService userService;


    @SubjectNotPresent
    @BodyParser.Of(BodyParser.TolerantJson.class)
    public Result login() throws ControllerException {
        JsonNode json = request().body().asJson();
        String username = json.findPath("user").textValue();
        String password = json.findPath("password").textValue();
        LOGGER.debug("API Try to login username = {}, password = {}", username, password);

        User user;
        try {
            user = userService.findByName(username);
        } catch (ServiceException e) {
            LOGGER.error("error = {}", e);
            throw new ControllerException(e.getMessage(), e);
        }

        if (user == null) {
            return unauthorized();
        }
        if (BCrypt.checkpw(password, user.password)) {
            TokenController.removeToken(response());
            TokenController.setToken(user, request().host(), response());
            return ok(Json.toJson(user.getRoles()));
        } else {
            return unauthorized();
        }
    }

    @SubjectPresent
    public Result roles() {
        User user = (User) Http.Context.current().args.get("user");
        LOGGER.debug("API Get roles for user = {}", user.toString());
        return ok(Json.toJson(user.getRoles()));
    }


    public Result logout() {
        LOGGER.debug("API Logout user");
        TokenController.removeToken(response());
        return ok();
    }
}
