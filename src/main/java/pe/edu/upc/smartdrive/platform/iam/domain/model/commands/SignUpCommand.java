package pe.edu.upc.smartdrive.platform.iam.domain.model.commands;

/**
 * Command expressing the intent to register a new company together with its
 * administrator account. Public sign-up now provisions a tenant (company + admin)
 * in a single operation.
 *
 * @param email        admin login email
 * @param password     admin plaintext password (hashed by the service)
 * @param fullName     admin full name
 * @param dni          admin DNI (encrypted at rest)
 * @param ruc          company tax id
 * @param phone        admin phone
 * @param businessName company legal/commercial name
 * @param companyDomain unique slug used to build seller usernames
 * @param maxWorkers   maximum number of sellers the company may register
 */
public record SignUpCommand(String email, String password, String fullName,
                            String dni, String ruc, String phone, String businessName,
                            String companyDomain, int maxWorkers) {
}
