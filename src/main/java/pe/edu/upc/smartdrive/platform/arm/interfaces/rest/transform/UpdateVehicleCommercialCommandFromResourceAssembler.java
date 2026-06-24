package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.UpdateVehicleCommercialResource;

/** Builds an {@link UpdateVehicleCommercialCommand} from the path id and REST resource. */
public class UpdateVehicleCommercialCommandFromResourceAssembler {
    public static UpdateVehicleCommercialCommand toCommandFromResource(String id, UpdateVehicleCommercialResource resource) {
        return new UpdateVehicleCommercialCommand(id, resource.vehicleId(), resource.userId(), resource.price(), resource.company());
    }
}
