package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleSpecification;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.VehicleSpecificationResource;

/** Maps a {@link VehicleSpecification} entity to its REST resource. */
public class VehicleSpecificationResourceFromEntityAssembler {
    public static VehicleSpecificationResource toResourceFromEntity(VehicleSpecification entity) {
        return new VehicleSpecificationResource(entity.getId(), entity.getVehicleId(), entity.getBrand(),
                entity.getModel(), entity.getYear(), entity.getTransmission());
    }
}
