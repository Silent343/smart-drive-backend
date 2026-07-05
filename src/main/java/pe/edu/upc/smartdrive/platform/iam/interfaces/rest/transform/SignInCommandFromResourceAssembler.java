package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources.SignInResource;

/** Maps a {@link SignInResource} request to a {@link SignInCommand}. */
public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.identifier(), resource.password());
    }
}
