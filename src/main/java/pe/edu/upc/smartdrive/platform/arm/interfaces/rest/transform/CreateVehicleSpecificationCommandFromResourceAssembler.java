package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.CreateVehicleSpecificationResource;

/** Builds a {@link CreateVehicleSpecificationCommand} from its REST resource. */
public class CreateVehicleSpecificationCommandFromResourceAssembler {
    public static CreateVehicleSpecificationCommand toCommandFromResource(CreateVehicleSpecificationResource resource) {
        return new CreateVehicleSpecificationCommand(resource.vehicleId(), resource.brand(), resource.model(),
                resource.year(), resource.transmission());
    }
}
