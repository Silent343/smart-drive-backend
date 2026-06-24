package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/**
 * Command expressing the intent to authenticate with email and password.
 */
public record SignInCommand(String email, String password) {
}
