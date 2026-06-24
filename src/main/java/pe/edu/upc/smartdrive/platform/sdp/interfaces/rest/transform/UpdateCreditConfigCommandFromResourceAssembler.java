package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.UpdateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.CreditConfigResource;

/** Builds an {@link UpdateCreditConfigCommand} using the id from the path and the resource body. */
public class UpdateCreditConfigCommandFromResourceAssembler {
    public static UpdateCreditConfigCommand toCommandFromResource(Long id, CreditConfigResource resource) {
        return new UpdateCreditConfigCommand(
                id,
                resource.currency(),
                resource.interestRateType(),
                resource.annualRate(),
                resource.capitalization(),
                resource.gracePeriodType(),
                resource.gracePeriodMonths(),
                resource.insuranceRatePct(),
                resource.postageFeeAmount(),
                resource.administrationFeePct());
    }
}
