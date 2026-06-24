package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.SimulatedLoan;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanResource;

/**
 * Maps a {@link Loan} entity (or a {@link SimulatedLoan} value object) to its REST resource.
 *
 * <p>A simulated loan has no persisted identity, so its resource carries a {@code null} id;
 * its indicators come from the computation instead of stored columns.</p>
 */
public class LoanResourceFromEntityAssembler {

    public static LoanResource toResourceFromEntity(Loan entity) {
        return new LoanResource(
                entity.getId(),
                entity.getCarId(),
                entity.getClientId(),
                entity.getConfigId(),
                entity.getInitialFee(),
                entity.getVehiclePrice(),
                entity.getLoanAmount(),
                entity.getInstallmentsQty(),
                entity.getStartDate(),
                entity.getFixedInstallment(),
                entity.getNpvDebtor(),
                entity.getIrrDebtor(),
                entity.getTcea(),
                entity.getTotalInterest(),
                entity.getTotalInsurance(),
                entity.getTotalPostage(),
                entity.getTotalCommission(),
                entity.getCtc());
    }

    public static LoanResource toResourceFromSimulated(SimulatedLoan simulated) {
        var inputs = simulated.inputs();
        var c = simulated.computation();
        return new LoanResource(
                null,
                inputs.carId(),
                inputs.clientId(),
                inputs.configId(),
                inputs.initialFee(),
                inputs.vehiclePrice(),
                inputs.loanAmount(),
                inputs.installmentsQty(),
                inputs.startDate(),
                c.fixedInstallment(),
                c.npvDebtor(),
                c.irrDebtor(),
                c.tcea(),
                c.totalInterest(),
                c.totalInsurance(),
                c.totalPostage(),
                c.totalCommission(),
                c.ctc());
    }
}
