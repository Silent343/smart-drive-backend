package pe.edu.upc.smartdrive.platform.arm.domain.services;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleSpecification;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleSpecificationCommand;

import java.util.Optional;

/** Command side of the VehicleSpecification aggregate. */
public interface VehicleSpecificationCommandService {
    Optional<VehicleSpecification> handle(CreateVehicleSpecificationCommand command);
    Optional<VehicleSpecification> handle(UpdateVehicleSpecificationCommand command);
    void handle(DeleteVehicleSpecificationCommand command);
}
