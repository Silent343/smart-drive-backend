package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/**
 * Command expressing an admin's intent to register a seller under their company.
 * The system derives the username from the company domain and the {@code code},
 * and generates the initial password (returned once to the admin).
 */
public record RegisterSellerCommand(Long companyId, String firstName, String lastName,
                                    String code, String dni, String phone) {
}
