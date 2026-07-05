package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/**
 * Command to authenticate with an identifier (admin email or seller username) and password.
 */
public record SignInCommand(String identifier, String password) {
}
