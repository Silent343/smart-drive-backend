package pe.edu.upc.smartdrive.platform.sdp.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.SimulateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.services.LoanCalculationService;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.LoanComputation;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.SimulatedLoan;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.LoanCommandService;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.CreditConfigRepository;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.LoanRepository;

import java.util.Optional;

/** Default {@link LoanCommandService}: persists confirmed loans and runs simulations. */
@Service
public class LoanCommandServiceImpl implements LoanCommandService {

    private final LoanRepository loanRepository;
    private final CreditConfigRepository creditConfigRepository;
    private final LoanCalculationService loanCalculationService;

    public LoanCommandServiceImpl(LoanRepository loanRepository,
                                  CreditConfigRepository creditConfigRepository,
                                  LoanCalculationService loanCalculationService) {
        this.loanRepository = loanRepository;
        this.creditConfigRepository = creditConfigRepository;
        this.loanCalculationService = loanCalculationService;
    }

    @Override
    public Optional<Loan> handle(CreateLoanCommand command) {
        var loan = new Loan(command);
        loanRepository.save(loan);
        return Optional.of(loan);
    }

    @Override
    public Optional<SimulatedLoan> handle(SimulateLoanCommand command) {
        return creditConfigRepository.findById(command.configId()).map(config -> {
            LoanComputation computation = loanCalculationService.calculate(
                    command.loanAmount(), command.installmentsQty(), command.startDate(), config, "pending");
            return new SimulatedLoan(command, computation);
        });
    }
}
