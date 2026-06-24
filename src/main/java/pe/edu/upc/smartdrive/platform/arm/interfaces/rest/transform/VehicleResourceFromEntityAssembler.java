package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Vehicle;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.VehicleResource;

/** Maps a {@link Vehicle} entity to its REST resource. */
public class VehicleResourceFromEntityAssembler {
    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        return new VehicleResource(entity.getId(), entity.getCode(), entity.getStatus(), entity.getImageUrl());
    }
}
