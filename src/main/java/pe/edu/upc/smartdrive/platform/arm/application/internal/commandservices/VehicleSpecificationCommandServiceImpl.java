package pe.edu.upc.smartdrive.platform.arm.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleSpecification;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleSpecificationCommandService;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleSpecificationRepository;

import java.util.Optional;

/** Default {@link VehicleSpecificationCommandService} backed by JPA persistence. */
@Service
public class VehicleSpecificationCommandServiceImpl implements VehicleSpecificationCommandService {

    private final VehicleSpecificationRepository repository;

    public VehicleSpecificationCommandServiceImpl(VehicleSpecificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<VehicleSpecification> handle(CreateVehicleSpecificationCommand command) {
        var spec = new VehicleSpecification(command);
        repository.save(spec);
        return Optional.of(spec);
    }

    @Override
    public Optional<VehicleSpecification> handle(UpdateVehicleSpecificationCommand command) {
        return repository.findById(command.id())
                .map(spec -> repository.save(spec.updateWith(command)));
    }

    @Override
    public void handle(DeleteVehicleSpecificationCommand command) {
        repository.deleteById(command.id());
    }
}
