package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/** REST representation of an amortization schedule row (snake_case). */
public record ScheduleRowResource(
        @JsonProperty("id") String id,
        @JsonProperty("loan_id") String loanId,
        @JsonProperty("installment_no") int installmentNo,
        @JsonProperty("payment_date") Instant paymentDate,
        @JsonProperty("opening_balance") double openingBalance,
        @JsonProperty("interest") double interest,
        @JsonProperty("amortization") double amortization,
        @JsonProperty("insurance") double insurance,
        @JsonProperty("postage") double postage,
        @JsonProperty("commission") double commission,
        @JsonProperty("monthly_payment") double monthlyPayment,
        @JsonProperty("ending_balance") double endingBalance,
        @JsonProperty("grace_period_type") String gracePeriodType) {
}
