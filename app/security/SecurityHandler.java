package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import models.User;
import play.Logger;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;

import static play.mvc.Results.forbidden;

/**
 * Created by Anton Chernov on 12/31/2015.
 */
public class SecurityHandler implements DeadboltHandler {
    private static final Logger.ALogger LOGGER = Logger.of(SecurityHandler.class);


    @Override
    public F.Promise<Optional<Result>> beforeAuthCheck(Http.Context context) {
        LOGGER.debug("Before check");
        return F.Promise.promise(Optional::empty);
    }


    @Override
    public F.Promise<Optional<Subject>> getSubject(Http.Context context) {
        LOGGER.debug("Try to authenticate api request");
        try {
            if (TokenController.validateToken(context)) {
                LOGGER.debug("Token is valid");
                User user = (User) context.args.get("user");
                TokenController.setToken(user, context.request().host(), context.response());
                return F.Promise.promise(() -> Optional.ofNullable(user));
            } else {
                LOGGER.debug("Token isn't valid");
                return F.Promise.promise(Optional::empty);
            }
        } catch (Throwable throwable) {
            LOGGER.debug("Token isn't valid");
            return F.Promise.promise(Optional::empty);
        }
    }


    @Override
    public F.Promise<Result> onAuthFailure(Http.Context context, String s) {
        LOGGER.debug("Authorization failed");
        return F.Promise.promise(() -> forbidden());
    }


    @Override
    public F.Promise<Optional<DynamicResourceHandler>> getDynamicResourceHandler(Http.Context context) {
        return null;
    }
}
