package pe.edu.upc.smartdrive.platform.sdp.domain.model.commands;

import java.time.Instant;

/**
 * Command to persist a confirmed loan, carrying inputs, ownership (company/seller/status)
 * and the full set of precomputed indicators and cost totals.
 */
public record CreateLoanCommand(String carId, String clientId, Long configId,
                                Long companyId, Long sellerId, String status,
                                double initialFee, double vehiclePrice, double loanAmount,
                                int installmentsQty, Instant startDate,
                                double fixedInstallment, double npvDebtor, double irrDebtor, double tcea, double trea,
                                double totalInterest, double totalInsurance, double totalRiskInsurance,
                                double totalGps, double totalPostage, double totalCommission, double totalTax,
                                double initialCosts, double residualValue, double ctc) {
}
