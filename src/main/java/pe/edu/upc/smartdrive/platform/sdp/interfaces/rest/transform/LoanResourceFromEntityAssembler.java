package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.SimulatedLoan;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanResource;

/**
 * Maps a {@link Loan} entity (or a {@link SimulatedLoan} value object) to its REST resource.
 * A simulated loan has no persisted identity, so its resource carries a {@code null} id and
 * its indicators come from the computation instead of stored columns.
 */
public class LoanResourceFromEntityAssembler {

    public static LoanResource toResourceFromEntity(Loan e) {
        return new LoanResource(
                e.getId(), e.getCarId(), e.getClientId(), e.getConfigId(), e.getSellerId(), e.getStatus(),
                e.getInitialFee(), e.getVehiclePrice(), e.getLoanAmount(), e.getInstallmentsQty(), e.getStartDate(),
                e.getFixedInstallment(), e.getNpvDebtor(), e.getIrrDebtor(), e.getTcea(), e.getTrea(),
                e.getTotalInterest(), e.getTotalInsurance(), e.getTotalRiskInsurance(), e.getTotalGps(),
                e.getTotalPostage(), e.getTotalCommission(), e.getTotalTax(), e.getInitialCosts(),
                e.getResidualValue(), e.getCtc());
    }

    public static LoanResource toResourceFromSimulated(SimulatedLoan simulated) {
        var i = simulated.inputs();
        var c = simulated.computation();
        return new LoanResource(
                null, i.carId(), i.clientId(), i.configId(), null, "SIMULATED",
                i.initialFee(), i.vehiclePrice(), i.loanAmount(), i.installmentsQty(), i.startDate(),
                c.fixedInstallment(), c.npvDebtor(), c.irrDebtor(), c.tcea(), c.trea(),
                c.totalInterest(), c.totalInsurance(), c.totalRiskInsurance(), c.totalGps(),
                c.totalPostage(), c.totalCommission(), c.totalTax(), c.initialCosts(),
                c.residualValue(), c.ctc());
    }
}
