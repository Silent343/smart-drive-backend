package pe.edu.upc.smartdrive.platform.arm.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Client;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllClientsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetClientByIdQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.services.ClientQueryService;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

/** Default {@link ClientQueryService} backed by JPA persistence. */
@Service
public class ClientQueryServiceImpl implements ClientQueryService {

    private final ClientRepository clientRepository;

    public ClientQueryServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> handle(GetAllClientsQuery query) {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> handle(GetClientByIdQuery query) {
        return clientRepository.findById(query.id());
    }
}
