package pe.edu.upc.smartdrive.platform.arm.domain.services;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Vehicle;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleCommand;

import java.util.Optional;

/** Command side of the Vehicle aggregate. */
public interface VehicleCommandService {
    Optional<Vehicle> handle(CreateVehicleCommand command);
    Optional<Vehicle> handle(UpdateVehicleCommand command);
    void handle(DeleteVehicleCommand command);
}
