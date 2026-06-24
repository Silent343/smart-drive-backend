package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanResource;

/**
 * Builds a {@link CreateLoanCommand} from a {@link LoanResource}. The loan is persisted as
 * supplied by the client (the frontend computes the indicators locally before confirming),
 * so every precomputed field is carried through; any supplied id is ignored.
 */
public class CreateLoanCommandFromResourceAssembler {
    public static CreateLoanCommand toCommandFromResource(LoanResource resource) {
        return new CreateLoanCommand(
                resource.carId(),
                resource.clientId(),
                resource.configId(),
                resource.initialFee(),
                resource.vehiclePrice(),
                resource.loanAmount(),
                resource.installmentsQty(),
                resource.startDate(),
                resource.fixedInstallment(),
                resource.npvDebtor(),
                resource.irrDebtor(),
                resource.tcea(),
                resource.totalInterest(),
                resource.totalInsurance(),
                resource.totalPostage(),
                resource.totalCommission(),
                resource.ctc());
    }
}
