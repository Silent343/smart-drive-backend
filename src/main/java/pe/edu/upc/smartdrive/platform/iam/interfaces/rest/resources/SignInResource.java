package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/**
 * Request body for authentication. {@code identifier} is the admin email or the
 * seller username; the backend resolves which scheme applies.
 */
public record SignInResource(String identifier, String password) {
}
