package pe.edu.upc.smartdrive.platform.sdp.domain.model.commands;

/** Command to update a credit configuration identified by {@code id}. */
public record UpdateCreditConfigCommand(Long id, String currency, String interestRateType, double annualRate,
                                        Integer capitalization, String gracePeriodType, int gracePeriodMonths,
                                        double insuranceRatePct, double postageFeeAmount, double administrationFeePct,
                                        double riskInsuranceRatePct, double gpsFeeAmount, double finalInstallmentPct,
                                        double igvItfPct, double notaryCostAmount, double registryCostAmount,
                                        double appraisalCostAmount, double studyCommissionAmount,
                                        double activationCommissionAmount, double discountAnnualRatePct) {
}
