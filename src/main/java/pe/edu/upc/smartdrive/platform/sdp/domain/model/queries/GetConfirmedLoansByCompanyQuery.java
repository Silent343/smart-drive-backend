package pe.edu.upc.smartdrive.platform.sdp.domain.model.queries;

/** Query to list the confirmed loans of a company (admin's confirmed-reports view). */
public record GetConfirmedLoansByCompanyQuery(Long companyId) {
}
