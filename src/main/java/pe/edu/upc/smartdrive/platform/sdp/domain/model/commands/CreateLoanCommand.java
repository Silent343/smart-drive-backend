package pe.edu.upc.smartdrive.platform.sdp.domain.model.commands;

import java.time.Instant;
import java.util.List;

/**
 * Command to persist a confirmed loan, carrying inputs, ownership (company/seller/status)
 * and the full set of precomputed indicators and cost totals.
 *
 * <p>Multi-vehicle: {@code vehicles} lists every financed vehicle with its price. When it is
 * null/empty the loan falls back to the single {@code carId}/{@code vehiclePrice}, so older
 * callers keep working. Use {@link #withVehicles} to attach the list without repeating all args.</p>
 */
public record CreateLoanCommand(String carId, String clientId, Long configId,
                                Long companyId, Long sellerId, String status,
                                double initialFee, double vehiclePrice, double loanAmount,
                                int installmentsQty, Instant startDate,
                                double fixedInstallment, double npvDebtor, double irrDebtor, double tcea, double trea,
                                double totalInterest, double totalInsurance, double totalRiskInsurance,
                                double totalGps, double totalPostage, double totalCommission, double totalTax,
                                double initialCosts, double residualValue, double ctc,
                                List<Vehicle> vehicles) {

    /** A financed vehicle inside the command: its ARM id and price. */
    public record Vehicle(String carId, double price) { }

    /** Normalizes {@code vehicles} to an empty list when null. */
    public CreateLoanCommand {
        vehicles = vehicles == null ? List.of() : vehicles;
    }

    /**
     * Backward-compatible factory matching the original 26-argument signature (no vehicle list).
     * The loan will synthesize a single-vehicle list from {@code carId}/{@code vehiclePrice}.
     */
    public static CreateLoanCommand single(String carId, String clientId, Long configId,
                                           Long companyId, Long sellerId, String status,
                                           double initialFee, double vehiclePrice, double loanAmount,
                                           int installmentsQty, Instant startDate,
                                           double fixedInstallment, double npvDebtor, double irrDebtor,
                                           double tcea, double trea,
                                           double totalInterest, double totalInsurance, double totalRiskInsurance,
                                           double totalGps, double totalPostage, double totalCommission, double totalTax,
                                           double initialCosts, double residualValue, double ctc) {
        return new CreateLoanCommand(carId, clientId, configId, companyId, sellerId, status,
                initialFee, vehiclePrice, loanAmount, installmentsQty, startDate,
                fixedInstallment, npvDebtor, irrDebtor, tcea, trea,
                totalInterest, totalInsurance, totalRiskInsurance, totalGps, totalPostage,
                totalCommission, totalTax, initialCosts, residualValue, ctc, List.of());
    }

    /** Returns a copy of this command with the given vehicle list attached. */
    public CreateLoanCommand withVehicles(List<Vehicle> vehicleList) {
        return new CreateLoanCommand(carId, clientId, configId, companyId, sellerId, status,
                initialFee, vehiclePrice, loanAmount, installmentsQty, startDate,
                fixedInstallment, npvDebtor, irrDebtor, tcea, trea,
                totalInterest, totalInsurance, totalRiskInsurance, totalGps, totalPostage,
                totalCommission, totalTax, initialCosts, residualValue, ctc, vehicleList);
    }
}
