package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Request body for email/password authentication. */
public record SignInResource(String email, String password) {
}
