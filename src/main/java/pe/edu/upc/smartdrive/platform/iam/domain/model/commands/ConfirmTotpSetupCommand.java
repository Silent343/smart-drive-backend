package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/**
 * Command to confirm TOTP enrolment by validating the first code from the user's app.
 */
public record ConfirmTotpSetupCommand(Long userId, String token) {
}
