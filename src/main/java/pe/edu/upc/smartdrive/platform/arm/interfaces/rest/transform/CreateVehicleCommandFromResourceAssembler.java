package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.CreateVehicleResource;

/** Builds a {@link CreateVehicleCommand} from its REST resource. */
public class CreateVehicleCommandFromResourceAssembler {
    public static CreateVehicleCommand toCommandFromResource(CreateVehicleResource resource) {
        return new CreateVehicleCommand(resource.code(), resource.status(), resource.imageUrl());
    }
}
