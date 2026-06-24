package pe.edu.upc.smartdrive.platform.sdp.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;

/** Spring Data repository for the {@link Loan} aggregate. */
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
