package pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates;

import jakarta.persistence.Entity;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableStringIdAggregateRoot;

/**
 * Vehicle aggregate root (ARM bounded context). Holds the catalogue entry a client
 * can be financed against. Technical and commercial details live in their own
 * aggregates linked by {@code vehicleId}.
 */
@Entity
public class Vehicle extends AuditableStringIdAggregateRoot {

    private String code;
    private String status;
    private String imageUrl;

    protected Vehicle() {
    }

    public Vehicle(CreateVehicleCommand command) {
        this.code = command.code();
        this.status = command.status();
        this.imageUrl = command.imageUrl();
    }

    public Vehicle updateWith(UpdateVehicleCommand command) {
        this.code = command.code();
        this.status = command.status();
        this.imageUrl = command.imageUrl();
        return this;
    }

    public String getCode() { return code; }
    public String getStatus() { return status; }
    public String getImageUrl() { return imageUrl; }
}
