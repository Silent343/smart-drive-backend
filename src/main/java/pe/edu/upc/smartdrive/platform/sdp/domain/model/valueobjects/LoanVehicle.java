package pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * One vehicle financed by a loan, as a value object embedded in the loan's child collection
 * ({@code loan_vehicle}). A credit can finance several vehicles at once; each row keeps the
 * vehicle's ARM id and the price captured at confirmation time, so the loan's total financed
 * amount is the sum of these prices and the report can list every vehicle.
 *
 * <p>Value objects have no identity of their own; they live and die with their owning loan.</p>
 */
@Embeddable
public class LoanVehicle {

    @Column(name = "car_id", nullable = false)
    private String carId;

    @Column(name = "price", nullable = false)
    private double price;

    protected LoanVehicle() {
    }

    public LoanVehicle(String carId, double price) {
        this.carId = carId;
        this.price = price;
    }

    public String getCarId() { return carId; }
    public double getPrice() { return price; }
}
