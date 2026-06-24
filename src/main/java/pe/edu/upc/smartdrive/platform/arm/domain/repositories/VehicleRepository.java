package pe.edu.upc.smartdrive.platform.arm.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Vehicle;

/** Spring Data repository for the {@link Vehicle} aggregate. */
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
