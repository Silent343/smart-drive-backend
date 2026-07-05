package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.UpdateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.CreditConfigResource;

/** Builds an {@link UpdateCreditConfigCommand} using the id from the path and the resource body. */
public class UpdateCreditConfigCommandFromResourceAssembler {
    public static UpdateCreditConfigCommand toCommandFromResource(Long id, CreditConfigResource r) {
        return new UpdateCreditConfigCommand(
                id, r.currency(), r.interestRateType(), r.annualRate(), r.capitalization(),
                r.gracePeriodType(), r.gracePeriodMonths(), r.insuranceRatePct(), r.postageFeeAmount(),
                r.administrationFeePct(), r.riskInsuranceRatePctOrZero(), r.gpsFeeAmountOrZero(),
                r.finalInstallmentPctOrZero(), r.igvItfPctOrZero(), r.notaryCostAmountOrZero(),
                r.registryCostAmountOrZero(), r.appraisalCostAmountOrZero(), r.studyCommissionAmountOrZero(),
                r.activationCommissionAmountOrZero(), r.discountAnnualRatePctOrZero());
    }
}
