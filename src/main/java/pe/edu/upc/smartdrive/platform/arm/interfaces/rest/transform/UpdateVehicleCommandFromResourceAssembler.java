package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.UpdateVehicleResource;

/** Builds an {@link UpdateVehicleCommand} from the path id and REST resource. */
public class UpdateVehicleCommandFromResourceAssembler {
    public static UpdateVehicleCommand toCommandFromResource(String id, UpdateVehicleResource resource) {
        return new UpdateVehicleCommand(id, resource.code(), resource.status(), resource.imageUrl());
    }
}
