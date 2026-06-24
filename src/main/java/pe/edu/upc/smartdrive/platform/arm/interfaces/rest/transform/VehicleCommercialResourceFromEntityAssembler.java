package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleCommercial;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.VehicleCommercialResource;

/** Maps a {@link VehicleCommercial} entity to its REST resource. */
public class VehicleCommercialResourceFromEntityAssembler {
    public static VehicleCommercialResource toResourceFromEntity(VehicleCommercial entity) {
        return new VehicleCommercialResource(entity.getId(), entity.getVehicleId(), entity.getUserId(),
                entity.getPrice(), entity.getCompany());
    }
}
