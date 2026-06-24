package pe.edu.upc.smartdrive.platform.sdp.domain.model.commands;

import java.time.Instant;

/** Command to persist a confirmed loan, carrying both inputs and precomputed indicators. */
public record CreateLoanCommand(String carId, String clientId, Long configId, double initialFee,
                                double vehiclePrice, double loanAmount, int installmentsQty, Instant startDate,
                                double fixedInstallment, double npvDebtor, double irrDebtor, double tcea,
                                double totalInterest, double totalInsurance, double totalPostage,
                                double totalCommission, double ctc) {
}
