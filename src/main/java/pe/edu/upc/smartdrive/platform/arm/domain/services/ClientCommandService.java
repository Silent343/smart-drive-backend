package pe.edu.upc.smartdrive.platform.arm.domain.services;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Client;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateClientCommand;

import java.util.Optional;

/** Command side of the Client aggregate. */
public interface ClientCommandService {
    Optional<Client> handle(CreateClientCommand command);
    Optional<Client> handle(UpdateClientCommand command);
    void handle(DeleteClientCommand command);
}
