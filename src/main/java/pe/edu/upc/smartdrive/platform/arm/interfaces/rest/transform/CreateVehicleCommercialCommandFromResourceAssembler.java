package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.CreateVehicleCommercialResource;

/** Builds a {@link CreateVehicleCommercialCommand} from its REST resource. */
public class CreateVehicleCommercialCommandFromResourceAssembler {
    public static CreateVehicleCommercialCommand toCommandFromResource(CreateVehicleCommercialResource resource) {
        return new CreateVehicleCommercialCommand(resource.vehicleId(), resource.userId(), resource.price(), resource.company());
    }
}
