package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.CreditConfigResource;

/** Maps a {@link CreditConfig} entity to its REST resource, including the extended cost fields. */
public class CreditConfigResourceFromEntityAssembler {
    public static CreditConfigResource toResourceFromEntity(CreditConfig e) {
        return new CreditConfigResource(
                e.getId(),
                e.getCurrency(),
                e.getInterestRateType(),
                e.getAnnualRate(),
                e.getCapitalization(),
                e.getGracePeriodType(),
                e.getGracePeriodMonths(),
                e.getInsuranceRatePct(),
                e.getPostageFeeAmount(),
                e.getAdministrationFeePct(),
                e.getRiskInsuranceRatePct(),
                e.getGpsFeeAmount(),
                e.getFinalInstallmentPct(),
                e.getIgvItfPct(),
                e.getNotaryCostAmount(),
                e.getRegistryCostAmount(),
                e.getAppraisalCostAmount(),
                e.getStudyCommissionAmount(),
                e.getActivationCommissionAmount(),
                e.getDiscountAnnualRatePct());
    }
}
