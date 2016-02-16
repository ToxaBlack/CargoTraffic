package dto;

import models.User;
import models.Waybill;

/**
 * Created by Maxim on 2/16/2016.
 */
public class WaybillDTO {

    public User manager;
    public Waybill waybill;

    public static WaybillDTO toWaybillDTO(Waybill waybill) {
        return null;
    }
}
