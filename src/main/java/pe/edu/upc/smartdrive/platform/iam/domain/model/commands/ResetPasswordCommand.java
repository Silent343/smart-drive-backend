package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/** Command to set a new password using a valid reset token. */
public record ResetPasswordCommand(String token, String newPassword) {
}
