package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/**
 * Successful authentication payload consumed by the frontend session store.
 * {@code role} is "ADMIN" or "SELLER" so the UI can render the correct label under
 * the user's name and gate admin-only views.
 */
public record AuthenticatedUserResource(String id, String identifier, String fullName,
                                        String role, String dni, String companyDomain,
                                        String token) {
}
