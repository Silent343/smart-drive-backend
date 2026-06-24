package pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableStringIdAggregateRoot;

/**
 * Vehicle technical specification aggregate (ARM bounded context). Linked to a
 * {@link Vehicle} via {@code vehicleId}.
 */
@Entity
public class VehicleSpecification extends AuditableStringIdAggregateRoot {

    private String vehicleId;
    private String brand;
    private String model;
    // "year" is a reserved word in MySQL/H2, so the column is explicitly quoted.
    @Column(name = "`year`")
    private Integer year;
    private String transmission;

    protected VehicleSpecification() {
    }

    public VehicleSpecification(CreateVehicleSpecificationCommand command) {
        this.vehicleId = command.vehicleId();
        this.brand = command.brand();
        this.model = command.model();
        this.year = command.year();
        this.transmission = command.transmission();
    }

    public VehicleSpecification updateWith(UpdateVehicleSpecificationCommand command) {
        this.vehicleId = command.vehicleId();
        this.brand = command.brand();
        this.model = command.model();
        this.year = command.year();
        this.transmission = command.transmission();
        return this;
    }

    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public Integer getYear() { return year; }
    public String getTransmission() { return transmission; }
}
