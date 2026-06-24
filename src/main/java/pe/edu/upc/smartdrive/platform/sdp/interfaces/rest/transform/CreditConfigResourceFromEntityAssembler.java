package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.CreditConfigResource;

/** Maps a {@link CreditConfig} entity to its REST resource. */
public class CreditConfigResourceFromEntityAssembler {
    public static CreditConfigResource toResourceFromEntity(CreditConfig entity) {
        return new CreditConfigResource(
                entity.getId(),
                entity.getCurrency(),
                entity.getInterestRateType(),
                entity.getAnnualRate(),
                entity.getCapitalization(),
                entity.getGracePeriodType(),
                entity.getGracePeriodMonths(),
                entity.getInsuranceRatePct(),
                entity.getPostageFeeAmount(),
                entity.getAdministrationFeePct());
    }
}
