package pe.edu.upc.smartdrive.platform.arm.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Vehicle;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehiclesQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleByIdQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleQueryService;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

/** Default {@link VehicleQueryService} backed by JPA persistence. */
@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> handle(GetAllVehiclesQuery query) {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.id());
    }
}
