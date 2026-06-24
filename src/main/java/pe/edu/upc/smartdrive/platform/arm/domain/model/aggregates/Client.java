package pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates;

import jakarta.persistence.Entity;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateClientCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableStringIdAggregateRoot;

/**
 * Client aggregate root (ARM bounded context).
 *
 * <p>Represents a credit applicant managed by an administrator. The identifier is a
 * short string token to stay compatible with the SmartDrive frontend, which links a
 * client to a vehicle through {@code vehicleId}.</p>
 */
@Entity
public class Client extends AuditableStringIdAggregateRoot {

    private String userId;
    private String name;
    private String dni;
    private Double income;
    private String occupation;
    private String phone;
    private String vehicleId;

    protected Client() {
    }

    public Client(CreateClientCommand command) {
        this.userId = command.userId();
        this.name = command.name();
        this.dni = command.dni();
        this.income = command.income();
        this.occupation = command.occupation();
        this.phone = command.phone();
        this.vehicleId = command.vehicleId();
    }

    /** Applies an update command, overwriting the editable attributes. */
    public Client updateWith(UpdateClientCommand command) {
        this.userId = command.userId();
        this.name = command.name();
        this.dni = command.dni();
        this.income = command.income();
        this.occupation = command.occupation();
        this.phone = command.phone();
        this.vehicleId = command.vehicleId();
        return this;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getDni() { return dni; }
    public Double getIncome() { return income; }
    public String getOccupation() { return occupation; }
    public String getPhone() { return phone; }
    public String getVehicleId() { return vehicleId; }
}
