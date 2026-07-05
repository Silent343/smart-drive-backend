package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/**
 * Response to a forgot-password request. Always reports success (to avoid revealing whether
 * the account exists). In this demo build, with no email service configured, the reset
 * {@code token} is returned directly so the flow can be exercised; in production it would be
 * emailed instead and this field would be omitted.
 */
public record ForgotPasswordResultResource(boolean requested, String token) {
}
