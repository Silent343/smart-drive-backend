package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/** Command to request a password reset for the account identified by email or username. */
public record ForgotPasswordCommand(String identifier) {
}
