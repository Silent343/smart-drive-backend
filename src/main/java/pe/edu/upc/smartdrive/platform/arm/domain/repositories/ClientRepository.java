package pe.edu.upc.smartdrive.platform.arm.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Client;

/** Spring Data repository for the {@link Client} aggregate. */
public interface ClientRepository extends JpaRepository<Client, String> {
}
