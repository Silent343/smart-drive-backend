package pe.edu.upc.smartdrive.platform.arm.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleCommercial;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleCommercialCommandService;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleCommercialRepository;

import java.util.Optional;

/** Default {@link VehicleCommercialCommandService} backed by JPA persistence. */
@Service
public class VehicleCommercialCommandServiceImpl implements VehicleCommercialCommandService {

    private final VehicleCommercialRepository repository;

    public VehicleCommercialCommandServiceImpl(VehicleCommercialRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<VehicleCommercial> handle(CreateVehicleCommercialCommand command) {
        var commercial = new VehicleCommercial(command);
        repository.save(commercial);
        return Optional.of(commercial);
    }

    @Override
    public Optional<VehicleCommercial> handle(UpdateVehicleCommercialCommand command) {
        return repository.findById(command.id())
                .map(commercial -> repository.save(commercial.updateWith(command)));
    }

    @Override
    public void handle(DeleteVehicleCommercialCommand command) {
        repository.deleteById(command.id());
    }
}
