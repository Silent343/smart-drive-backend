package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateClientCommand;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.UpdateClientResource;

/** Builds an {@link UpdateClientCommand} from the path id and REST resource. */
public class UpdateClientCommandFromResourceAssembler {
    public static UpdateClientCommand toCommandFromResource(String id, UpdateClientResource resource) {
        return new UpdateClientCommand(id, resource.userId(), resource.name(), resource.dni(), resource.income(),
                resource.occupation(), resource.phone(), resource.vehicleId());
    }
}
