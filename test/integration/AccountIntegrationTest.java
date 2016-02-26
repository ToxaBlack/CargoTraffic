package integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import exception.ServiceException;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.FORBIDDEN;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

/**
 * Created by Anton Chernov on 2/10/2016.
 */
public class AccountIntegrationTest extends BaseIntegrationTest {
    @Test
    public void testBadRoute() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/api/account");

        Result result = route(request);
        assertEquals(FORBIDDEN, result.status());
    }


    @Test
    public void testGet() throws ServiceException {
        Http.RequestBuilder request = getAutoriseRequest("SYS_ADMIN").method(GET).uri("/api/account");

        Result result = route(request);
        assertEquals("application/json", result.contentType());
        assertEquals("utf-8", result.charset());
        assertEquals("{\"username\":\"sys_admin\",\"name\":\"poll\",\"surname\":\"simson\"," +
                "\"patronymic\":\"васильевич\",\"birthday\":\"1994-01-06\",\"email\":\"test@mail.ru\"," +
                "\"country\":\"Belarus\",\"city\":\"Minsk\",\"street\":\"blabla\",\"house\":\"12\"," +
                "\"flat\":\"1\"}",contentAsString(result));

        assertEquals(OK, result.status());
    }

    @Test
    public void testUpdate() throws ServiceException {
        JsonNode jsonNode = Json.parse("{\"username\":\"sys_admin\",\"name\":\"tom\",\"surname\":\"simson\"," +
                "\"patronymic\":\"васильевич\",\"birthday\":\"1994-01-06\",\"email\":\"test@mail.ru\"," +
                "\"country\":\"Belarus\",\"city\":\"Minsk\",\"street\":\"blabla\",\"house\":\"12\"," +
                "\"flat\":\"1\"}");

        Http.RequestBuilder request = getAutoriseRequest("SYS_ADMIN").method(PUT).uri("/api/account").bodyJson(jsonNode);

        Result result = route(request);
        assertEquals(OK, result.status());
    }

}
