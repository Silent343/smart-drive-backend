package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST representation of a credit configuration. Field names are snake_case to match the
 * SmartDrive frontend's CreditConfig assembler. Used for both input and output; on create
 * the {@code id} is ignored (assigned by the server) and on update it comes from the path.
 *
 * <p>The extended cost fields (all-risk insurance, GPS, residual value, IGV/ITF, initial
 * costs and the discount rate) are optional wrappers: when the client omits them they
 * default to 0, keeping older frontend versions working.</p>
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
        @JsonProperty("administration_fee_pct") double administrationFeePct,
        @JsonProperty("risk_insurance_rate_pct") Double riskInsuranceRatePct,
        @JsonProperty("gps_fee_amount") Double gpsFeeAmount,
        @JsonProperty("final_installment_pct") Double finalInstallmentPct,
        @JsonProperty("igv_itf_pct") Double igvItfPct,
        @JsonProperty("notary_cost_amount") Double notaryCostAmount,
        @JsonProperty("registry_cost_amount") Double registryCostAmount,
        @JsonProperty("appraisal_cost_amount") Double appraisalCostAmount,
        @JsonProperty("study_commission_amount") Double studyCommissionAmount,
        @JsonProperty("activation_commission_amount") Double activationCommissionAmount,
        @JsonProperty("discount_annual_rate_pct") Double discountAnnualRatePct) {

    /** Null-safe accessor: returns 0.0 when the optional field was omitted by the client. */
    private static double orZero(Double v) { return v == null ? 0.0 : v; }

    public double riskInsuranceRatePctOrZero() { return orZero(riskInsuranceRatePct); }
    public double gpsFeeAmountOrZero() { return orZero(gpsFeeAmount); }
    public double finalInstallmentPctOrZero() { return orZero(finalInstallmentPct); }
    public double igvItfPctOrZero() { return orZero(igvItfPct); }
    public double notaryCostAmountOrZero() { return orZero(notaryCostAmount); }
    public double registryCostAmountOrZero() { return orZero(registryCostAmount); }
    public double appraisalCostAmountOrZero() { return orZero(appraisalCostAmount); }
    public double studyCommissionAmountOrZero() { return orZero(studyCommissionAmount); }
    public double activationCommissionAmountOrZero() { return orZero(activationCommissionAmount); }
    public double discountAnnualRatePctOrZero() { return orZero(discountAnnualRatePct); }
}
