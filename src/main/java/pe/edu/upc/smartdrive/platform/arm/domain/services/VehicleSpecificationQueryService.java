package pe.edu.upc.smartdrive.platform.arm.domain.services;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleSpecification;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehicleSpecificationsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleSpecificationByIdQuery;

import java.util.List;
import java.util.Optional;

/** Query side of the VehicleSpecification aggregate. */
public interface VehicleSpecificationQueryService {
    List<VehicleSpecification> handle(GetAllVehicleSpecificationsQuery query);
    Optional<VehicleSpecification> handle(GetVehicleSpecificationByIdQuery query);
}
