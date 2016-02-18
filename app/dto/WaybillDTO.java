package dto;

import models.*;
import models.statuses.WaybillStatus;
import models.statuses.WaypointStatus;
import service.ServiceException;
import service.UserService;

import java.util.*;

/**
 * Created by Maxim on 2/16/2016.
 */
public class WaybillDTO {
    public UserDTO manager;
    public Date departureDate;
    public Date arrivalDate;
    public Set<UserDTO> drivers;
    public Set<Vehicle> vehicles;
    public Set<VehicleDriverDTO> vehicleDrivers;
    public PackingListDTO packingList;
    public Set<Waypoint> waypoints;
    public static WaybillDTO toWaybillDTO(User manager, Waybill waybill, List<User> drivers, List<Vehicle> vehicles) {

        WaybillDTO waybillDTO = new WaybillDTO();
        waybillDTO.vehicles = new HashSet<>();
        waybillDTO.drivers = new HashSet<>();
        waybillDTO.vehicleDrivers = new HashSet<>();

        waybillDTO.manager = UserDTO.toUserDTO(manager);

        for (User driver : drivers)
            waybillDTO.drivers.add(UserDTO.toUserDTO(driver));
        for (Vehicle vehicle : vehicles)
            waybillDTO.vehicles.add(vehicle);

        if(waybill!=null) {
            waybillDTO.departureDate = waybill.departureDate;
            waybillDTO.arrivalDate = waybill.arrivalDate;
            waybillDTO.packingList = PackingListDTO.toPackingListDTO(waybill.packingList);
            for (WaybillVehicleDriver vd : waybill.vehicleDrivers) {
                waybillDTO.vehicleDrivers.add(VehicleDriverDTO.toVehicleDriverDTO(vd));
                waybillDTO.drivers.remove(UserDTO.toUserDTO(vd.driver));
                waybillDTO.vehicles.remove(vd.vehicle);
            }
            waybillDTO.waypoints = waybill.waypoints;
        }
        return waybillDTO;
    }
    public static WaybillDTO toWaybillDTO(User manager, PackingList packingList, List<User> drivers, List<Vehicle> vehicles) {
        WaybillDTO waybillDTO = new WaybillDTO();

        waybillDTO.manager = UserDTO.toUserDTO(manager);
        waybillDTO.vehicles = new HashSet<>();
        waybillDTO.drivers = new HashSet<>();

        for (User driver : drivers)
            waybillDTO.drivers.add(UserDTO.toUserDTO(driver));
        for (Vehicle vehicle : vehicles)
            waybillDTO.vehicles.add(vehicle);

        waybillDTO.packingList = PackingListDTO.toPackingListDTO(packingList);
        return waybillDTO;
    }


    public Waybill toWaybill() throws ServiceException {
        Waybill waybill = new Waybill();
        waybill.departureDate = this.departureDate;
        waybill.arrivalDate = this.arrivalDate;
        waybill.manager = (new UserService()).find(manager.id);
        waybill.packingList = new PackingList();
        waybill.packingList.id = packingList.id;

        waybill.status = WaybillStatus.TRANSPORTATION_STARTED;
        waybill.vehicleDrivers = new HashSet<>();
        List<ProductInWaybill> productsInWaybill;
        WaybillVehicleDriver wvd;
        ProductInWaybill piw;
        for(VehicleDriverDTO vd : this.vehicleDrivers)
        {
            productsInWaybill = new ArrayList<>();
            wvd = vd.toWaybillVehicleDriver();
            for(ProductDTO prod : vd.products)
            {
                piw = new ProductInWaybill(prod.toProduct(),prod.quantity);
                piw.waybillVehicleDriver = wvd;
                productsInWaybill.add(piw);
            }

            wvd.productsInWaybill = productsInWaybill;
            wvd.waybill =  waybill;
            waybill.vehicleDrivers.add(wvd);
        }
        waybill.waypoints = this.waypoints;
        for(Waypoint wp : waybill.waypoints)
        {
            wp.status=WaypointStatus.CREATED;
            wp.waybill = waybill;
        }
        return waybill;
    }
}
