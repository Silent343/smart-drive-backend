package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * REST representation of a loan. Field names are snake_case to match the frontend's Loan
 * assembler. Note the deliberate spelling {@code instalments_qty} (single 'l'), required
 * for compatibility with the existing client.
 */
public record LoanResource(
        @JsonProperty("id") Long id,
        @JsonProperty("car_id") String carId,
        @JsonProperty("client_id") String clientId,
        @JsonProperty("config_id") Long configId,
        @JsonProperty("initial_fee") double initialFee,
        @JsonProperty("vehicle_price") double vehiclePrice,
        @JsonProperty("loan_amount") double loanAmount,
        @JsonProperty("instalments_qty") int installmentsQty,
        @JsonProperty("start_date") Instant startDate,
        @JsonProperty("fixed_installment") double fixedInstallment,
        @JsonProperty("npv_debtor") double npvDebtor,
        @JsonProperty("irr_debtor") double irrDebtor,
        @JsonProperty("tcea") double tcea,
        @JsonProperty("total_interest") double totalInterest,
        @JsonProperty("total_insurance") double totalInsurance,
        @JsonProperty("total_postage") double totalPostage,
        @JsonProperty("total_commission") double totalCommission,
        @JsonProperty("ctc") double ctc) {
}
