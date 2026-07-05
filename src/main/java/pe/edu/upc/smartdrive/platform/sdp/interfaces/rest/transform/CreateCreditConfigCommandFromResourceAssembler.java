package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.CreditConfigResource;

/** Builds a {@link CreateCreditConfigCommand} from the incoming resource; any supplied id is ignored. */
public class CreateCreditConfigCommandFromResourceAssembler {
    public static CreateCreditConfigCommand toCommandFromResource(CreditConfigResource r) {
        return new CreateCreditConfigCommand(
                r.currency(), r.interestRateType(), r.annualRate(), r.capitalization(),
                r.gracePeriodType(), r.gracePeriodMonths(), r.insuranceRatePct(), r.postageFeeAmount(),
                r.administrationFeePct(), r.riskInsuranceRatePctOrZero(), r.gpsFeeAmountOrZero(),
                r.finalInstallmentPctOrZero(), r.igvItfPctOrZero(), r.notaryCostAmountOrZero(),
                r.registryCostAmountOrZero(), r.appraisalCostAmountOrZero(), r.studyCommissionAmountOrZero(),
                r.activationCommissionAmountOrZero(), r.discountAnnualRatePctOrZero());
    }
}
