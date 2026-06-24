package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Successful authentication payload consumed by the frontend session store. */
public record AuthenticatedUserResource(String id, String email, String fullName,
                                        String role, String dni, String token) {
}
