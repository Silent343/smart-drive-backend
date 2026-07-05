package pe.edu.upc.smartdrive.platform.iam.domain.model.queries;

/** Query to list the sellers belonging to a company. */
public record GetSellersByCompanyQuery(Long companyId) {
}
