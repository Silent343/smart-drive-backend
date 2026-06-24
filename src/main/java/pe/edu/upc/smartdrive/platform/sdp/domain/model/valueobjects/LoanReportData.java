package pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;

import java.util.List;

/** Aggregated data for a loan report: the loan, its configuration and the schedule. */
public record LoanReportData(Loan loan, CreditConfig config, List<ScheduleRow> schedule) {
}
