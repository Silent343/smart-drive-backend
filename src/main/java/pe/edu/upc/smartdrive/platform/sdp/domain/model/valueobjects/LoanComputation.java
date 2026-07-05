package pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects;

import java.util.List;

/**
 * Result of running the French amortization engine: the computed financial indicators plus
 * the full payment schedule. Extended with the Interbank-style cost totals (all-risk
 * insurance, GPS, tax), the initial costs, the residual (balloon) value of the Compra
 * Inteligente modality, and the investor-side TREA (effective annual return at the
 * opportunity discount rate). Used by the simulate endpoint and to rebuild a persisted
 * loan's schedule/report.
 */
public record LoanComputation(double fixedInstallment, double npvDebtor, double irrDebtor, double tcea,
                              double trea,
                              double totalInterest, double totalInsurance, double totalRiskInsurance,
                              double totalGps, double totalPostage, double totalCommission, double totalTax,
                              double initialCosts, double residualValue, double ctc,
                              List<ScheduleRow> schedule) {
}
