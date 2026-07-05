package pe.edu.upc.smartdrive.platform.sdp.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;

import java.util.List;

/** Spring Data repository for the {@link Loan} aggregate. */
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /** Loans of a company in a given status (e.g. CONFIRMED), newest first. */
    List<Loan> findByCompanyIdAndStatusOrderByIdDesc(Long companyId, String status);

    /** Loans created by a specific seller in a given status. */
    List<Loan> findBySellerIdAndStatusOrderByIdDesc(Long sellerId, String status);
}
