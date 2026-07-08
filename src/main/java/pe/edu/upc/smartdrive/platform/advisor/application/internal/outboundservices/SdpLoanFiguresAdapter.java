package pe.edu.upc.smartdrive.platform.advisor.application.internal.outboundservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.AdvisorLoanFigures;
import pe.edu.upc.smartdrive.platform.advisor.domain.services.LoanFiguresProvider;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanByIdQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.LoanQueryService;

import java.util.Optional;

/**
 * Adapter that implements {@link LoanFiguresProvider} by reading the SDP
 * {@link Loan} aggregate and mapping its figures onto an
 * {@link AdvisorLoanFigures} value object.
 *
 * <p>This is the anti-corruption layer between the advisor and SDP contexts:
 * the advisor depends on the {@link LoanFiguresProvider} port, and only this
 * adapter knows SDP's model. The grounding format itself lives in
 * {@link AdvisorLoanFigures}, so a persisted loan and a simulated one produce
 * identical context.</p>
 */
@Service
public class SdpLoanFiguresAdapter implements LoanFiguresProvider {

    private final LoanQueryService loanQueryService;

    /**
     * @param loanQueryService SDP's query service to load loans by id
     */
    public SdpLoanFiguresAdapter(LoanQueryService loanQueryService) {
        this.loanQueryService = loanQueryService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<LoanFigures> figuresFor(Long loanId) {
        if (loanId == null) {
            return Optional.empty();
        }
        Optional<Loan> maybeLoan = loanQueryService.handle(new GetLoanByIdQuery(loanId));
        return maybeLoan.map(this::toFigures).map(AdvisorLoanFigures::toLoanFigures);
    }

    /**
     * Maps an SDP {@link Loan} onto the advisor's {@link AdvisorLoanFigures}.
     *
     * @param loan the loan aggregate
     * @return the advisor figures value object
     */
    private AdvisorLoanFigures toFigures(Loan loan) {
        return new AdvisorLoanFigures(
                "S/",
                loan.getVehiclePrice(),
                loan.getInitialFee(),
                loan.getLoanAmount(),
                loan.getInstallmentsQty(),
                loan.getFixedInstallment(),
                loan.getTcea(),
                loan.getTotalInterest(),
                loan.getTotalInsurance(),
                loan.getTotalPostage(),
                loan.getTotalCommission(),
                loan.getCtc(),
                loan.getNpvDebtor(),
                loan.getIrrDebtor());
    }
}
