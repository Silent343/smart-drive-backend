package pe.edu.upc.smartdrive.platform.arm.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleCommercial;

/** Spring Data repository for the {@link VehicleCommercial} aggregate. */
public interface VehicleCommercialRepository extends JpaRepository<VehicleCommercial, String> {
}
