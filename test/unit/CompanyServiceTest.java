package unit;

import exception.ServiceException;
import models.Company;
import models.User;
import models.UserRole;
import org.junit.Test;
import play.Logger;
import play.test.FakeApplication;
import play.test.WithApplication;
import service.CompanyService;
import service.UserService;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

/**
 * Created by Anton on 04.05.2016.
 */
public class CompanyServiceTest extends WithApplication {
    private static final Logger.ALogger LOGGER = Logger.of(UserServiceTest.class);

    @Override
    protected FakeApplication provideFakeApplication() {
        return fakeApplication(inMemoryDatabase("cargo_traffic"));
    }

    @Test
    public void testFindCompany() throws ServiceException {
        CompanyService companyService = app.injector().instanceOf(CompanyService.class);
        Company company = companyService.getCompanies(1, 5, true).get(0);
        assertEquals(1, company.id);
    }

    @Test
    public void testSaveCompany() throws ServiceException {
        CompanyService companyService = app.injector().instanceOf(CompanyService.class);
        Company company = new Company("test", new Date(), 23D, false);
        companyService.addClient(company, TestUtils.getUser());
        assertNotNull(company.id);
    }
}
