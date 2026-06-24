package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Client;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.ClientResource;

/** Maps a {@link Client} entity to its REST resource. */
public class ClientResourceFromEntityAssembler {
    public static ClientResource toResourceFromEntity(Client entity) {
        return new ClientResource(entity.getId(), entity.getUserId(), entity.getName(), entity.getDni(),
                entity.getIncome(), entity.getOccupation(), entity.getPhone(), entity.getVehicleId());
    }
}
