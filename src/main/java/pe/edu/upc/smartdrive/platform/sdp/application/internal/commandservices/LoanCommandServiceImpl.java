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
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.shared.domain.validation.InputValidator;

import java.util.Optional;

/**
 * Default {@link LoanCommandService}: runs simulations and persists confirmed loans.
 *
 * <p>On confirmation the indicators and cost totals are <strong>recomputed on the server</strong>
 * from the loan inputs and the credit configuration, rather than trusting whatever the client
 * posts. This keeps the stored numbers consistent with the current calculation rules.</p>
 */
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

    /**
     * Validates the loan inputs against the business rules E1, E2, E4 and E6 of the report:
     * price &gt; 0, down payment &lt; price (financed amount &gt; 0), term &gt; 0 and grace &lt; term.
     */
    private void validateLoanInputs(double vehiclePrice, double initialFee, double loanAmount,
                                    int installmentsQty, CreditConfig config) {
        InputValidator.requirePositive(vehiclePrice, "precio del vehículo");        // E1
        InputValidator.requireNonNegative(initialFee, "cuota inicial");
        if (initialFee >= vehiclePrice)                                             // E2
            throw new IllegalArgumentException("El monto a financiar debe ser mayor a 0 (la cuota inicial no puede igualar o superar el precio)");
        InputValidator.requirePositive(loanAmount, "monto a financiar");
        if (installmentsQty <= 0)                                                    // E4
            throw new IllegalArgumentException("El plazo debe ser un entero mayor a 0");
        if (config.getGracePeriodMonths() >= installmentsQty)                        // E6
            throw new IllegalArgumentException("Los meses de gracia deben ser menores al plazo");
    }

    @Override
    public Optional<Loan> handle(CreateLoanCommand command) {
        return creditConfigRepository.findById(command.configId()).map(config -> {
            validateLoanInputs(command.vehiclePrice(), command.initialFee(), command.loanAmount(),
                    command.installmentsQty(), config);
            LoanComputation c = loanCalculationService.calculate(
                    command.loanAmount(), command.installmentsQty(), command.startDate(),
                    config, command.vehiclePrice(), "pending");

            // Recompose the command with the server-computed indicators, preserving the inputs
            // and ownership fields the caller supplied.
            var enriched = new CreateLoanCommand(
                    command.carId(), command.clientId(), command.configId(),
                    command.companyId(), command.sellerId(), command.status(),
                    command.initialFee(), command.vehiclePrice(), command.loanAmount(),
                    command.installmentsQty(), command.startDate(),
                    c.fixedInstallment(), c.npvDebtor(), c.irrDebtor(), c.tcea(), c.trea(),
                    c.totalInterest(), c.totalInsurance(), c.totalRiskInsurance(), c.totalGps(),
                    c.totalPostage(), c.totalCommission(), c.totalTax(),
                    c.initialCosts(), c.residualValue(), c.ctc());

            var loan = new Loan(enriched);
            loanRepository.save(loan);
            return loan;
        });
    }

    @Override
    public Optional<SimulatedLoan> handle(SimulateLoanCommand command) {
        return creditConfigRepository.findById(command.configId()).map(config -> {
            validateLoanInputs(command.vehiclePrice(), command.initialFee(), command.loanAmount(),
                    command.installmentsQty(), config);
            LoanComputation computation = loanCalculationService.calculate(
                    command.loanAmount(), command.installmentsQty(), command.startDate(),
                    config, command.vehiclePrice(), "pending");
            return new SimulatedLoan(command, computation);
        });
    }
}
