package pe.edu.upc.smartdrive.platform.sdp.domain.model.queries;

/** Query to (re)generate the amortization schedule of a persisted loan. */
public record GetLoanScheduleQuery(Long loanId) {
}
