package models;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "financial_highlights")
public class FinancialHighlights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "delivered_date")
    public Date deliveredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waybill_vehicle_driver_id",
            referencedColumnName = "id")
    public WaybillVehicleDriver waybillVehicleDriver;

    @Column(name = "transportation_income")
    public Double transportationIncome;

    @Column(name = "vehicle_fuel_loss")
    public Double vehicleFuelLoss;

    @Column(name = "products_loss")
    public Double productsLoss;
}
