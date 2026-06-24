package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/**
 * Command to verify a TOTP code during a two-factor sign-in.
 */
public record VerifyTotpCommand(Long userId, String token) {
}
