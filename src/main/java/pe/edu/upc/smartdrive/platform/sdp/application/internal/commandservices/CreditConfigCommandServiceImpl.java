package pe.edu.upc.smartdrive.platform.sdp.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.UpdateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.CreditConfigCommandService;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.CreditConfigRepository;
import pe.edu.upc.smartdrive.platform.shared.domain.validation.InputValidator;

import java.util.Optional;
import java.util.Set;

/** Default {@link CreditConfigCommandService} backed by JPA persistence. */
@Service
public class CreditConfigCommandServiceImpl implements CreditConfigCommandService {

    private static final Set<String> CURRENCIES = Set.of("PEN", "USD");
    private static final Set<String> GRACE_TYPES = Set.of("none", "partial", "total");

    private final CreditConfigRepository creditConfigRepository;

    public CreditConfigCommandServiceImpl(CreditConfigRepository creditConfigRepository) {
        this.creditConfigRepository = creditConfigRepository;
    }

    /** Validates the currency, rate, residual and grace configuration (rules E3, E5 and more). */
    private void validate(String currency, double annualRate, String gracePeriodType,
                          int gracePeriodMonths, double finalInstallmentPct, double igvItfPct) {
        if (!CURRENCIES.contains(currency))
            throw new IllegalArgumentException("Solo se aceptan las monedas PEN o USD");   // E5
        if (!GRACE_TYPES.contains(gracePeriodType))
            throw new IllegalArgumentException("El tipo de gracia debe ser none, partial o total");
        InputValidator.requirePositive(annualRate, "tasa de interés");
        InputValidator.requireNonNegative(gracePeriodMonths, "meses de gracia");
        InputValidator.requireRange(finalInstallmentPct, 0, 50, "valor residual (%)"); // E3
        InputValidator.requireRange(igvItfPct, 0, 100, "IGV/ITF (%)");
    }

    @Override
    public Optional<CreditConfig> handle(CreateCreditConfigCommand command) {
        validate(command.currency(), command.annualRate(), command.gracePeriodType(),
                command.gracePeriodMonths(), command.finalInstallmentPct(), command.igvItfPct());
        var config = new CreditConfig(command);
        creditConfigRepository.save(config);
        return Optional.of(config);
    }

    @Override
    public Optional<CreditConfig> handle(UpdateCreditConfigCommand command) {
        validate(command.currency(), command.annualRate(), command.gracePeriodType(),
                command.gracePeriodMonths(), command.finalInstallmentPct(), command.igvItfPct());
        return creditConfigRepository.findById(command.id())
                .map(config -> creditConfigRepository.save(config.updateWith(command)));
    }
}
