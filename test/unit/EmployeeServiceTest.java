package unit;

import exception.ServiceException;
import models.User;
import org.junit.Test;
import play.Logger;
import play.test.FakeApplication;
import play.test.WithApplication;
import service.EmployeesService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

/**
 * Created by Anton on 05.05.2016.
 */
public class EmployeeServiceTest extends WithApplication {
    private static final Logger.ALogger LOGGER = Logger.of(UserServiceTest.class);

    @Override
    protected FakeApplication provideFakeApplication() {
        return fakeApplication(inMemoryDatabase("cargo_traffic"));
    }


    @Test
    public void testFindEmployee() throws ServiceException {
        EmployeesService EmployeeService = app.injector().instanceOf(EmployeesService.class);
        User Employee = EmployeeService.getEmployee(1L);
        assertEquals(1L, (long) Employee.id);
    }

    @Test
    public void testSaveEmployee() throws ServiceException {
        EmployeesService EmployeeService = app.injector().instanceOf(EmployeesService.class);
        User Employee = TestUtils.getUser();
        Employee.name = "test";
        EmployeeService.addEmployee(Employee);
        assertNotNull(Employee.id);
    }
}
