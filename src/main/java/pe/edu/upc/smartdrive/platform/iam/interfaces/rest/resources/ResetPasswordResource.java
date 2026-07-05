package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Request body to complete a password reset. */
public record ResetPasswordResource(String token, String newPassword) {
}
