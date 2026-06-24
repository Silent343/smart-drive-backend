package pe.edu.upc.smartdrive.platform.arm.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Client;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.services.ClientCommandService;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.ClientRepository;

import java.util.Optional;

/** Default {@link ClientCommandService} backed by JPA persistence. */
@Service
public class ClientCommandServiceImpl implements ClientCommandService {

    private final ClientRepository clientRepository;

    public ClientCommandServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> handle(CreateClientCommand command) {
        var client = new Client(command);
        clientRepository.save(client);
        return Optional.of(client);
    }

    @Override
    public Optional<Client> handle(UpdateClientCommand command) {
        return clientRepository.findById(command.id())
                .map(client -> clientRepository.save(client.updateWith(command)));
    }

    @Override
    public void handle(DeleteClientCommand command) {
        clientRepository.deleteById(command.id());
    }
}
