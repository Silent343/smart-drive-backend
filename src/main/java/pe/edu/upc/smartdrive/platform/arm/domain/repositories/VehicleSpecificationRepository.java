package pe.edu.upc.smartdrive.platform.arm.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleSpecification;

/** Spring Data repository for the {@link VehicleSpecification} aggregate. */
public interface VehicleSpecificationRepository extends JpaRepository<VehicleSpecification, String> {
}
