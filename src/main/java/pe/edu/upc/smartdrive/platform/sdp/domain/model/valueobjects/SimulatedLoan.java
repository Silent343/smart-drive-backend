package pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.SimulateLoanCommand;

/** A simulated (non-persisted) loan: the original inputs plus the computed indicators. */
public record SimulatedLoan(SimulateLoanCommand inputs, LoanComputation computation) {
}
