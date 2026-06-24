package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/**
 * Command expressing the intent to register a new administrator account.
 */
public record SignUpCommand(String email, String password, String fullName,
                            String dni, String ruc, String phone, String businessName) {
}
