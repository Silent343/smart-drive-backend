package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

/** Builds the authenticated-user payload (including the issued JWT) from a {@link User}. */
public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String companyDomain, String token) {
        return new AuthenticatedUserResource(
                user.getPublicId(),
                user.loginHandle(),
                user.getFullName(),
                user.getRole().name(),
                user.getDni() == null ? "Sin DNI" : user.getDni(),
                companyDomain,
                token);
    }
}
