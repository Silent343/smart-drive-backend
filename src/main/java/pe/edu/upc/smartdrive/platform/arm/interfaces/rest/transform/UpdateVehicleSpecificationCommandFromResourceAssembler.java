package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.UpdateVehicleSpecificationResource;

/** Builds an {@link UpdateVehicleSpecificationCommand} from the path id and REST resource. */
public class UpdateVehicleSpecificationCommandFromResourceAssembler {
    public static UpdateVehicleSpecificationCommand toCommandFromResource(String id, UpdateVehicleSpecificationResource resource) {
        return new UpdateVehicleSpecificationCommand(id, resource.vehicleId(), resource.brand(), resource.model(),
                resource.year(), resource.transmission());
    }
}
