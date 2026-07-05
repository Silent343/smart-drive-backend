package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Request body to start a password reset (email or username). */
public record ForgotPasswordResource(String identifier) {
}
