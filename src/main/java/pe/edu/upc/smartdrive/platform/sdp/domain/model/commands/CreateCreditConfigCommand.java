package pe.edu.upc.smartdrive.platform.sdp.domain.model.commands;

/**
 * Command to create a credit configuration. Beyond the base parameters it carries the
 * Interbank-style cost breakdown: all-risk insurance, GPS, residual value (Compra
 * Inteligente), IGV/ITF tax, one-off initial costs and the opportunity discount rate.
 */
public record CreateCreditConfigCommand(String currency, String interestRateType, double annualRate,
                                        Integer capitalization, String gracePeriodType, int gracePeriodMonths,
                                        double insuranceRatePct, double postageFeeAmount, double administrationFeePct,
                                        double riskInsuranceRatePct, double gpsFeeAmount, double finalInstallmentPct,
                                        double igvItfPct, double notaryCostAmount, double registryCostAmount,
                                        double appraisalCostAmount, double studyCommissionAmount,
                                        double activationCommissionAmount, double discountAnnualRatePct) {

    /**
     * Backward-compatible factory matching the original 9-argument signature; the new fields
     * default to 0. Lets existing callers (e.g. the demo seeder) keep working unchanged.
     */
    public static CreateCreditConfigCommand basic(String currency, String interestRateType, double annualRate,
                                                  Integer capitalization, String gracePeriodType, int gracePeriodMonths,
                                                  double insuranceRatePct, double postageFeeAmount,
                                                  double administrationFeePct) {
        return new CreateCreditConfigCommand(currency, interestRateType, annualRate, capitalization, gracePeriodType,
                gracePeriodMonths, insuranceRatePct, postageFeeAmount, administrationFeePct,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }
}
