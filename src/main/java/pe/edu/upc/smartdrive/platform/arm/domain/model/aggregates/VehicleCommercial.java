package pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates;

import jakarta.persistence.Entity;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableStringIdAggregateRoot;

/**
 * Vehicle commercial information aggregate (ARM bounded context). Holds the price and
 * dealer for a {@link Vehicle}, linked via {@code vehicleId}.
 */
@Entity
public class VehicleCommercial extends AuditableStringIdAggregateRoot {

    private String vehicleId;
    private String userId;
    private Double price;
    private String company;

    protected VehicleCommercial() {
    }

    public VehicleCommercial(CreateVehicleCommercialCommand command) {
        this.vehicleId = command.vehicleId();
        this.userId = command.userId();
        this.price = command.price();
        this.company = command.company();
    }

    public VehicleCommercial updateWith(UpdateVehicleCommercialCommand command) {
        this.vehicleId = command.vehicleId();
        this.userId = command.userId();
        this.price = command.price();
        this.company = command.company();
        return this;
    }

    public String getVehicleId() { return vehicleId; }
    public String getUserId() { return userId; }
    public Double getPrice() { return price; }
    public String getCompany() { return company; }
}
