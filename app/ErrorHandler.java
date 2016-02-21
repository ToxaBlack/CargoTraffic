import play.Configuration;
import play.Environment;
import play.Logger;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by Anton Chernov on 1/9/2016.
 */
public class ErrorHandler extends DefaultHttpErrorHandler {
    private static final Logger.ALogger LOGGER = Logger.of(ErrorHandler.class);
    @Inject
    public ErrorHandler(Configuration configuration, Environment environment,
                        OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }

    protected void logServerError(Http.RequestHeader request,
                                  play.api.UsefulException usefulException) {
        LOGGER.error("Exception = {}", usefulException.getMessage());
    }
}
