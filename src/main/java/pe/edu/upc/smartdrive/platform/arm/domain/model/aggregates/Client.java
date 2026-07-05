package pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateClientCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableStringIdAggregateRoot;
import pe.edu.upc.smartdrive.platform.shared.infrastructure.persistence.jpa.crypto.EncryptedStringConverter;

/**
 * Client aggregate root (ARM bounded context).
 *
 * <p>Represents a credit applicant managed by an administrator or seller. The identifier is a
 * short string token to stay compatible with the SmartDrive frontend.</p>
 *
 * <p><strong>Multi-vehicle change:</strong> a client is no longer tied to a single vehicle.
 * A client can request quotes/simulations for several vehicles, so the client→vehicle link
 * now lives on each {@code Loan} (its {@code carId}) instead of here. The legacy
 * {@code vehicleId} column is kept nullable and no longer required, purely so older frontend
 * payloads that still send it do not break; it is not used to constrain anything.</p>
 *
 * <p>The {@code dni} is stored encrypted at rest via {@link EncryptedStringConverter}.</p>
 */
@Entity
public class Client extends AuditableStringIdAggregateRoot {

    private String userId;
    private String name;

    /** Encrypted at rest. */
    @Convert(converter = EncryptedStringConverter.class)
    private String dni;

    private Double income;
    private String occupation;
    private String phone;

    /** @deprecated legacy single-vehicle link; the real link is now on each Loan. Kept only for input compatibility. */
    @Deprecated
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

    /** @deprecated see class note; the client→vehicle link now lives on each Loan. */
    @Deprecated
    public String getVehicleId() { return vehicleId; }
}
