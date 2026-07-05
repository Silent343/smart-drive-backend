package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources.SignUpResource;

/** Maps a {@link SignUpResource} request to a {@link SignUpCommand}. */
public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        int maxWorkers = resource.maxWorkers() == null ? 5 : resource.maxWorkers();
        return new SignUpCommand(resource.email(), resource.password(), resource.fullName(),
                resource.dni(), resource.ruc(), resource.phone(), resource.businessName(),
                resource.companyDomain(), maxWorkers);
    }
}
