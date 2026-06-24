package pe.edu.upc.smartdrive.platform.sdp.domain.services;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.SimulateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.SimulatedLoan;

import java.util.Optional;

/** Command side of the Loan aggregate. */
public interface LoanCommandService {
    /** Persists a confirmed loan exactly as supplied by the client. */
    Optional<Loan> handle(CreateLoanCommand command);

    /** Computes a loan's indicators and schedule without persisting anything. */
    Optional<SimulatedLoan> handle(SimulateLoanCommand command);
}
