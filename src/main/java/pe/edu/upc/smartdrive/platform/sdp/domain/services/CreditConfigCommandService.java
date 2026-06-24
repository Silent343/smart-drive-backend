package pe.edu.upc.smartdrive.platform.sdp.domain.services;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.UpdateCreditConfigCommand;

import java.util.Optional;

/** Command side of the CreditConfig aggregate. */
public interface CreditConfigCommandService {
    Optional<CreditConfig> handle(CreateCreditConfigCommand command);
    Optional<CreditConfig> handle(UpdateCreditConfigCommand command);
}
