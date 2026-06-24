package pe.edu.upc.smartdrive.platform.advisor.application.internal.outboundservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.advisor.domain.services.LoanFiguresProvider;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanByIdQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.LoanQueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Adapter that implements {@link LoanFiguresProvider} by reading the SDP
 * {@link Loan} aggregate and formatting its figures into a grounding block.
 *
 * <p>This is the anti-corruption layer between the advisor and SDP contexts:
 * the advisor depends on the {@link LoanFiguresProvider} port, and only this
 * adapter knows SDP's model.</p>
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
        Optional<Loan> maybeLoan = loanQueryService.handle(new GetLoanByIdQuery(loanId));
        return maybeLoan.map(this::format);
    }

    /**
     * Formats a loan into a grounding text block and a list of figure labels.
     *
     * @param loan the loan aggregate
     * @return the formatted figures
     */
    private LoanFigures format(Loan loan) {
        List<String> labels = new ArrayList<>();

        String groundingText = """
                Cifras del crédito vehicular (todas en soles, salvo indicación):
                - Precio del vehículo: %s
                - Cuota inicial: %s
                - Monto financiado (préstamo): %s
                - Número de cuotas: %d
                - Cuota fija mensual: %s
                - TCEA (Tasa de Costo Efectivo Anual): %s %%
                - Interés total a pagar: %s
                - Seguro de desgravamen total: %s
                - Portes totales: %s
                - Comisiones totales: %s
                - Costo total del crédito (CTC): %s
                - VAN del deudor: %s
                - TIR del deudor: %s %%
                """.formatted(
                money(loan.getVehiclePrice()),
                money(loan.getInitialFee()),
                money(loan.getLoanAmount()),
                loan.getInstallmentsQty(),
                money(loan.getFixedInstallment()),
                rate(loan.getTcea()),
                money(loan.getTotalInterest()),
                money(loan.getTotalInsurance()),
                money(loan.getTotalPostage()),
                money(loan.getTotalCommission()),
                money(loan.getCtc()),
                money(loan.getNpvDebtor()),
                rate(loan.getIrrDebtor()));

        labels.add("Cuota mensual: " + money(loan.getFixedInstallment()));
        labels.add("TCEA: " + rate(loan.getTcea()) + " %");
        labels.add("Costo total: " + money(loan.getCtc()));
        labels.add("N° cuotas: " + loan.getInstallmentsQty());

        return new LoanFigures(groundingText, labels);
    }

    /**
     * Formats a monetary amount in Peruvian soles.
     *
     * @param value the amount
     * @return a string like {@code "S/ 1,234.56"}
     */
    private String money(double value) {
        return String.format(Locale.US, "S/ %,.2f", value);
    }

    /**
     * Formats a percentage rate with two decimals.
     *
     * @param value the rate (already expressed as a percentage)
     * @return a string like {@code "18.25"}
     */
    private String rate(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}
