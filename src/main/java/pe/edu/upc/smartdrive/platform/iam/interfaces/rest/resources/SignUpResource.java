package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Request body for user registration. */
public record SignUpResource(String email, String password, String fullName,
                             String dni, String ruc, String phone, String businessName) {
}
