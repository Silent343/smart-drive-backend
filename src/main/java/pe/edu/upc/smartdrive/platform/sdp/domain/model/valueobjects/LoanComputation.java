package pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects;

import java.util.List;

/**
 * Result of running the French amortization engine: the computed financial indicators
 * plus the full payment schedule. Used both by the simulate endpoint and to rebuild a
 * persisted loan's schedule/report.
 */
public record LoanComputation(double fixedInstallment, double npvDebtor, double irrDebtor, double tcea,
                              double totalInterest, double totalInsurance, double totalPostage,
                              double totalCommission, double ctc, List<ScheduleRow> schedule) {
}
