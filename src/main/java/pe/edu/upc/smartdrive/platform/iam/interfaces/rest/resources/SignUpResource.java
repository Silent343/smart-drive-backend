package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/**
 * Request body for company registration (public sign-up). Provisions the company
 * (tenant) together with its administrator account.
 */
public record SignUpResource(String email, String password, String fullName,
                             String dni, String ruc, String phone, String businessName,
                             String companyDomain, Integer maxWorkers) {
}
