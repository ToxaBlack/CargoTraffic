package unit;

import exception.ServiceException;
import models.Vehicle;
import models.Warehouse;
import org.junit.Before;
import org.junit.Test;
import play.Logger;
import play.mvc.Http;
import play.test.FakeApplication;
import play.test.WithApplication;
import service.WarehouseService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

/**
 * Created by Anton on 05.05.2016.
 */
public class WarehouseServiceTest extends WithApplication {
    private static final Logger.ALogger LOGGER = Logger.of(UserServiceTest.class);

    @Override
    protected FakeApplication provideFakeApplication() {
        return fakeApplication(inMemoryDatabase("cargo_traffic"));
    }


    @Test
    public void testFindWarehouse() throws ServiceException {
        WarehouseService warehouseService = app.injector().instanceOf(WarehouseService.class);
        Warehouse warehouse = warehouseService.getWarhouses(1L, 5, true).get(0);
        assertEquals(1L, (long) warehouse.id);
    }

    @Test
    public void testSaveWarehouse() throws ServiceException {
        WarehouseService warehouseService = app.injector().instanceOf(WarehouseService.class);
        Warehouse warehouse = new Warehouse();
        warehouse.name = "test";
        warehouseService.addWarehouse(warehouse);
        assertNotNull(warehouse.id);
    }
}