package pe.edu.upc.smartdrive.platform.arm.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleCommercial;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehicleCommercialsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleCommercialByIdQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleCommercialQueryService;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleCommercialRepository;

import java.util.List;
import java.util.Optional;

/** Default {@link VehicleCommercialQueryService} backed by JPA persistence. */
@Service
public class VehicleCommercialQueryServiceImpl implements VehicleCommercialQueryService {

    private final VehicleCommercialRepository repository;

    public VehicleCommercialQueryServiceImpl(VehicleCommercialRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<VehicleCommercial> handle(GetAllVehicleCommercialsQuery query) {
        return repository.findAll();
    }

    @Override
    public Optional<VehicleCommercial> handle(GetVehicleCommercialByIdQuery query) {
        return repository.findById(query.id());
    }
}
