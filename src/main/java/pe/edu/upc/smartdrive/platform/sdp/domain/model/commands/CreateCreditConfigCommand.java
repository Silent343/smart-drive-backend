package pe.edu.upc.smartdrive.platform.sdp.domain.model.commands;

/** Command to create a credit configuration. */
public record CreateCreditConfigCommand(String currency, String interestRateType, double annualRate,
                                        Integer capitalization, String gracePeriodType, int gracePeriodMonths,
                                        double insuranceRatePct, double postageFeeAmount, double administrationFeePct) {
}
