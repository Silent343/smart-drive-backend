package pe.edu.upc.smartdrive.platform.sdp.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.UpdateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.CreditConfigCommandService;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.CreditConfigRepository;

import java.util.Optional;

/** Default {@link CreditConfigCommandService} backed by JPA persistence. */
@Service
public class CreditConfigCommandServiceImpl implements CreditConfigCommandService {

    private final CreditConfigRepository creditConfigRepository;

    public CreditConfigCommandServiceImpl(CreditConfigRepository creditConfigRepository) {
        this.creditConfigRepository = creditConfigRepository;
    }

    @Override
    public Optional<CreditConfig> handle(CreateCreditConfigCommand command) {
        var config = new CreditConfig(command);
        creditConfigRepository.save(config);
        return Optional.of(config);
    }

    @Override
    public Optional<CreditConfig> handle(UpdateCreditConfigCommand command) {
        return creditConfigRepository.findById(command.id())
                .map(config -> creditConfigRepository.save(config.updateWith(command)));
    }
}
