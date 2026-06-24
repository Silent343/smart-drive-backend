package pe.edu.upc.smartdrive.platform.arm.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleSpecification;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehicleSpecificationsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleSpecificationByIdQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleSpecificationQueryService;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleSpecificationRepository;

import java.util.List;
import java.util.Optional;

/** Default {@link VehicleSpecificationQueryService} backed by JPA persistence. */
@Service
public class VehicleSpecificationQueryServiceImpl implements VehicleSpecificationQueryService {

    private final VehicleSpecificationRepository repository;

    public VehicleSpecificationQueryServiceImpl(VehicleSpecificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<VehicleSpecification> handle(GetAllVehicleSpecificationsQuery query) {
        return repository.findAll();
    }

    @Override
    public Optional<VehicleSpecification> handle(GetVehicleSpecificationByIdQuery query) {
        return repository.findById(query.id());
    }
}
