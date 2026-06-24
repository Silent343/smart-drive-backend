package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateClientCommand;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.CreateClientResource;

/** Builds a {@link CreateClientCommand} from its REST resource. */
public class CreateClientCommandFromResourceAssembler {
    public static CreateClientCommand toCommandFromResource(CreateClientResource resource) {
        return new CreateClientCommand(resource.userId(), resource.name(), resource.dni(), resource.income(),
                resource.occupation(), resource.phone(), resource.vehicleId());
    }
}
