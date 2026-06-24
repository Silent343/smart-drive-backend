package pe.edu.upc.smartdrive.platform.arm.domain.services;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Vehicle;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehiclesQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleByIdQuery;

import java.util.List;
import java.util.Optional;

/** Query side of the Vehicle aggregate. */
public interface VehicleQueryService {
    List<Vehicle> handle(GetAllVehiclesQuery query);
    Optional<Vehicle> handle(GetVehicleByIdQuery query);
}
