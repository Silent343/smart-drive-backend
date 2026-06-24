package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.CreditConfigResource;

/** Builds a {@link CreateCreditConfigCommand} from the incoming resource; any supplied id is ignored. */
public class CreateCreditConfigCommandFromResourceAssembler {
    public static CreateCreditConfigCommand toCommandFromResource(CreditConfigResource resource) {
        return new CreateCreditConfigCommand(
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
