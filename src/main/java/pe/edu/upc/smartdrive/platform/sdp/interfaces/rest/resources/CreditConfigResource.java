package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST representation of a credit configuration. Field names are snake_case to match the
 * SmartDrive frontend's CreditConfig assembler. Used for both input and output; on create
 * the {@code id} is ignored (assigned by the server) and on update it comes from the path.
 */
public record CreditConfigResource(
        @JsonProperty("id") Long id,
        @JsonProperty("currency") String currency,
        @JsonProperty("interest_rate_type") String interestRateType,
        @JsonProperty("annual_rate") double annualRate,
        @JsonProperty("capitalization") Integer capitalization,
        @JsonProperty("grace_period_type") String gracePeriodType,
        @JsonProperty("grace_period_months") int gracePeriodMonths,
        @JsonProperty("insurance_rate_pct") double insuranceRatePct,
        @JsonProperty("postage_fee_amount") double postageFeeAmount,
        @JsonProperty("administration_fee_pct") double administrationFeePct) {
}
