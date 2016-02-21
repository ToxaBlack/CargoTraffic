package integration;

import models.User;
import org.junit.Before;
import play.mvc.Http;
import play.mvc.Http.RequestBuilder;
import play.test.FakeApplication;
import play.test.WithApplication;
import security.TokenController;
import exception.ServiceException;
import service.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static play.test.Helpers.fakeApplication;

/**
 * Created by Anton Chernov on 2/8/2016.
 */
public class BaseIntegrationTest extends WithApplication {

    @Override
    protected FakeApplication provideFakeApplication() {
        Map<String, String> conf = new HashMap<>();
        conf.put("applyEvolutions.default", "false");
        conf.put("applyEvolutions.test", "true");
        conf.put("db.cargo_traffic.url", "jdbc:h2:mem:play;MODE=MYSQL");
        conf.put("db.cargo_traffic.driver", "org.h2.Driver");
        return fakeApplication(conf);
    }



    @Before
    public void setUp() throws Exception {
        Map<String, String> flashData = Collections.emptyMap();
        Map<String, Object> argData = Collections.emptyMap();
        Long id = 2L;
        play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
        Http.Request request = mock(Http.Request.class);
        Http.Context context = new Http.Context(id, header, request, flashData, flashData, argData);
        Http.Context.current.set(context);
    }

    protected RequestBuilder getAutoriseRequest(String userRole) throws ServiceException {
        UserService userService = new UserService();
        User user = userService.findByUsername(userRole);
        Http.Context.current().args.put("user", user);
        Http.Cookie cookie = new Http.Cookie(TokenController.COOKIE_NAME,
                TokenController.createToken(user, "localhost"), 100, null, null, false, false);
        RequestBuilder request = new RequestBuilder();
        return request.cookie(cookie);
    }


}



