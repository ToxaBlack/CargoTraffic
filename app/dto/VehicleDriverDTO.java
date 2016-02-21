package dto;

import models.ProductInWaybill;
import models.Vehicle;
import models.WaybillVehicleDriver;
import exception.ServiceException;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxim on 2/17/2016.
 */
public class VehicleDriverDTO {
    public UserDTO driver;
    public Vehicle vehicle;
    public List<ProductDTO> products;

    public static VehicleDriverDTO toVehicleDriverDTO(WaybillVehicleDriver wvd){
        VehicleDriverDTO vehicleDriverDTO = new VehicleDriverDTO();
        vehicleDriverDTO.driver = UserDTO.toUserDTO(wvd.driver);
        vehicleDriverDTO.vehicle = wvd.vehicle;
        vehicleDriverDTO.products = new ArrayList<>();
        for(ProductInWaybill productInWaybill : wvd.productsInWaybill){
            vehicleDriverDTO.products.add(new ProductDTO(productInWaybill.product,productInWaybill.quantity));
        }
        return vehicleDriverDTO;
    }

    public WaybillVehicleDriver toWaybillVehicleDriver() throws ServiceException {
        WaybillVehicleDriver wvd = new WaybillVehicleDriver();
        wvd.driver = (new UserService()).find(this.driver.id);
        wvd.vehicle = this.vehicle;
        return wvd;
    }
}
