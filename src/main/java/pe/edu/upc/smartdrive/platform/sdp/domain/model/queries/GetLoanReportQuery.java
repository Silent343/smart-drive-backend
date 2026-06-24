package pe.edu.upc.smartdrive.platform.sdp.domain.model.queries;

/** Query to build the full report (loan + config + schedule) of a persisted loan. */
public record GetLoanReportQuery(Long loanId) {
}
