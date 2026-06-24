package pe.edu.upc.smartdrive.platform.sdp.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanByIdQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanReportQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanScheduleQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.services.LoanCalculationService;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.LoanComputation;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.LoanReportData;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.ScheduleRow;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.LoanQueryService;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.CreditConfigRepository;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.LoanRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Default {@link LoanQueryService}. The schedule and report are derived data: they are
 * regenerated on demand by re-running the amortization engine over the loan's inputs and
 * its credit configuration.
 */
@Service
public class LoanQueryServiceImpl implements LoanQueryService {

    private final LoanRepository loanRepository;
    private final CreditConfigRepository creditConfigRepository;
    private final LoanCalculationService loanCalculationService;

    public LoanQueryServiceImpl(LoanRepository loanRepository,
                                CreditConfigRepository creditConfigRepository,
                                LoanCalculationService loanCalculationService) {
        this.loanRepository = loanRepository;
        this.creditConfigRepository = creditConfigRepository;
        this.loanCalculationService = loanCalculationService;
    }

    @Override
    public Optional<Loan> handle(GetLoanByIdQuery query) {
        return loanRepository.findById(query.id());
    }

    @Override
    public List<ScheduleRow> handle(GetLoanScheduleQuery query) {
        return loanRepository.findById(query.loanId())
                .flatMap(loan -> creditConfigRepository.findById(loan.getConfigId())
                        .map(config -> compute(loan, config).schedule()))
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<LoanReportData> handle(GetLoanReportQuery query) {
        return loanRepository.findById(query.loanId())
                .flatMap(loan -> creditConfigRepository.findById(loan.getConfigId())
                        .map(config -> new LoanReportData(loan, config, compute(loan, config).schedule())));
    }

    private LoanComputation compute(Loan loan, pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig config) {
        return loanCalculationService.calculate(
                loan.getLoanAmount(), loan.getInstallmentsQty(), loan.getStartDate(),
                config, String.valueOf(loan.getId()));
    }
}
