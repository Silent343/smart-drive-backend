package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Read model for a seller shown in the admin's worker-management view. */
public record SellerResource(String id, String username, String fullName, String firstName,
                             String lastName, String code, String dni, String phone) {
}
