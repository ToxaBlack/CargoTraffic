package unit;

import exception.ServiceException;
import models.Company;
import models.User;
import models.UserRole;
import models.Vehicle;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import play.Logger;
import play.mvc.Http;
import play.test.FakeApplication;
import play.test.WithApplication;
import service.CompanyService;
import service.VehicleService;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

/**
 * Created by Anton on 05.05.2016.
 */
@Ignore
public class VehicleServiceTest extends WithApplication {
    private static final Logger.ALogger LOGGER = Logger.of(UserServiceTest.class);

    @Override
    protected FakeApplication provideFakeApplication() {
        return fakeApplication(inMemoryDatabase("cargo_traffic"));
    }

    @Before
    public void setUp() throws Exception {
        Http.Context context = mock(Http.Context.class);
        context.args.put("user", TestUtils.getUser());
        Http.Context.current.set(context);
    }

    @Test
    public void testFindVehicle() throws ServiceException {
        VehicleService vehicleService = app.injector().instanceOf(VehicleService.class);
        Vehicle vehicle = vehicleService.getVehicles(1L, 5, true).get(0);
        assertEquals(1L, (long)vehicle.id);
    }

    @Test
    public void testSaveUser() throws ServiceException {
        VehicleService vehicleService = app.injector().instanceOf(VehicleService.class);
        Vehicle vehicle = new Vehicle();
        vehicleService.addVehicle(vehicle);
        assertNotNull(vehicle.id);
    }
}