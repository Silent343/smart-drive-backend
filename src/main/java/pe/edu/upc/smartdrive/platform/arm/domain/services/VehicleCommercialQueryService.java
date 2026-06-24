package pe.edu.upc.smartdrive.platform.arm.domain.services;

import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleCommercial;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehicleCommercialsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleCommercialByIdQuery;

import java.util.List;
import java.util.Optional;

/** Query side of the VehicleCommercial aggregate. */
public interface VehicleCommercialQueryService {
    List<VehicleCommercial> handle(GetAllVehicleCommercialsQuery query);
    Optional<VehicleCommercial> handle(GetVehicleCommercialByIdQuery query);
}
