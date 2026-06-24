package pe.edu.upc.smartdrive.platform.arm.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Vehicle;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.UpdateVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleCommandService;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleRepository;

import java.util.Optional;

/** Default {@link VehicleCommandService} backed by JPA persistence. */
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> handle(CreateVehicleCommand command) {
        var vehicle = new Vehicle(command);
        vehicleRepository.save(vehicle);
        return Optional.of(vehicle);
    }

    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        return vehicleRepository.findById(command.id())
                .map(vehicle -> vehicleRepository.save(vehicle.updateWith(command)));
    }

    @Override
    public void handle(DeleteVehicleCommand command) {
        vehicleRepository.deleteById(command.id());
    }
}
