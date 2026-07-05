package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/**
 * Response returned once when a seller is registered, carrying the generated username and
 * initial password so the admin can hand them to the seller. The password is not stored in
 * plaintext and cannot be retrieved again.
 */
public record SellerRegistrationResultResource(String id, String username, String initialPassword,
                                               String fullName, String code) {
}
