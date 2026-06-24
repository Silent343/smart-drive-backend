package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.SimulateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanResource;

/**
 * Builds a {@link SimulateLoanCommand} from a {@link LoanResource}. Only the inputs are
 * read; the indicators in the body (if any) are recomputed by the engine.
 */
public class SimulateLoanCommandFromResourceAssembler {
    public static SimulateLoanCommand toCommandFromResource(LoanResource resource) {
        return new SimulateLoanCommand(
                resource.carId(),
                resource.clientId(),
                resource.configId(),
                resource.initialFee(),
                resource.vehiclePrice(),
                resource.loanAmount(),
                resource.installmentsQty(),
                resource.startDate());
    }
}
