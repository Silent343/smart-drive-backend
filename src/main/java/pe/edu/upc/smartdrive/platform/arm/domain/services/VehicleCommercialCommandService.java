package pe.edu.upc.smartdrive.platform.arm.domain.services;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleCommercial;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleCommercialCommand;

import java.util.Optional;

/** Command side of the VehicleCommercial aggregate. */
public interface VehicleCommercialCommandService {
    Optional<VehicleCommercial> handle(CreateVehicleCommercialCommand command);
    Optional<VehicleCommercial> handle(UpdateVehicleCommercialCommand command);
    void handle(DeleteVehicleCommercialCommand command);
}
