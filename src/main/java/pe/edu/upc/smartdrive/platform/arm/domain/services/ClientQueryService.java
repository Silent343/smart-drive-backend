package pe.edu.upc.smartdrive.platform.arm.domain.services;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Client;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllClientsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetClientByIdQuery;

import java.util.List;
import java.util.Optional;

/** Query side of the Client aggregate. */
public interface ClientQueryService {
    List<Client> handle(GetAllClientsQuery query);
    Optional<Client> handle(GetClientByIdQuery query);
}
