package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/**
 * Command to start TOTP enrolment for a user, generating a new secret and QR code.
 */
public record SetupTotpCommand(Long userId) {
}
