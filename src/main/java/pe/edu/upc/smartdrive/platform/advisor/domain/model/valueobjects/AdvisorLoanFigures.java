package pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects;

import pe.edu.upc.smartdrive.platform.advisor.domain.services.LoanFiguresProvider.LoanFigures;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The raw financial figures of a loan the advisor grounds its answers on.
 *
 * <p>This value object is the single source of truth for the grounding
 * representation. Both origins of figures use it:</p>
 * <ul>
 *   <li>a <strong>confirmed</strong> loan, mapped from the SDP aggregate by the
 *       {@code SdpLoanFiguresAdapter}; and</li>
 *   <li>a <strong>simulated</strong> loan, sent inline in the request before the
 *       credit is persisted (so there is no {@code loanId} yet).</li>
 * </ul>
 *
 * <p>Keeping the format here — instead of duplicating it per adapter — means the
 * model receives an identical, consistent context in both cases, which is what
 * lets it answer the same way for a simulation as for a saved loan.</p>
 *
 * <p>Monetary fields are amounts in the loan's currency; {@code tceaPct} and
 * {@code irrDebtorPct} are already expressed as percentages (e.g. {@code 18.25}
 * for 18.25 %), so callers must convert fractions before constructing this.</p>
 *
 * @param currencySymbol   the currency symbol to prefix amounts with (e.g. "S/")
 * @param vehiclePrice     the vehicle price
 * @param initialFee       the down payment (cuota inicial)
 * @param loanAmount       the financed amount (vehiclePrice - initialFee)
 * @param installmentsQty  the number of installments
 * @param fixedInstallment the fixed monthly installment
 * @param tceaPct          the TCEA, as a percentage
 * @param totalInterest    the total interest to pay
 * @param totalInsurance   the total credit-life insurance (desgravamen)
 * @param totalPostage     the total postage (portes)
 * @param totalCommission  the total commissions
 * @param ctc              the total cost of credit (CTC)
 * @param npvDebtor        the debtor's net present value (VAN)
 * @param irrDebtorPct     the debtor's IRR (TIR), as a percentage
 */
public record AdvisorLoanFigures(
        String currencySymbol,
        double vehiclePrice,
        double initialFee,
        double loanAmount,
        int installmentsQty,
        double fixedInstallment,
        double tceaPct,
        double totalInterest,
        double totalInsurance,
        double totalPostage,
        double totalCommission,
        double ctc,
        double npvDebtor,
        double irrDebtorPct) {

    /**
     * Renders these figures into the port's {@link LoanFigures}: a grounding
     * text block plus the short labels shown to the user for transparency.
     *
     * @return the ready-to-ground figures
     */
    public LoanFigures toLoanFigures() {
        String symbol = currencySymbol == null || currencySymbol.isBlank()
                ? "S/"
                : currencySymbol;

        String groundingText = """
                Cifras del crédito vehicular (montos en %s):
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
                symbol,
                money(symbol, vehiclePrice),
                money(symbol, initialFee),
                money(symbol, loanAmount),
                installmentsQty,
                money(symbol, fixedInstallment),
                rate(tceaPct),
                money(symbol, totalInterest),
                money(symbol, totalInsurance),
                money(symbol, totalPostage),
                money(symbol, totalCommission),
                money(symbol, ctc),
                money(symbol, npvDebtor),
                rate(irrDebtorPct));

        List<String> labels = new ArrayList<>();
        labels.add("Cuota mensual: " + money(symbol, fixedInstallment));
        labels.add("TCEA: " + rate(tceaPct) + " %");
        labels.add("Costo total: " + money(symbol, ctc));
        labels.add("N° cuotas: " + installmentsQty);

        return new LoanFigures(groundingText, labels);
    }

    /**
     * Formats a monetary amount with the given currency symbol.
     *
     * @param symbol the currency symbol
     * @param value  the amount
     * @return a string like {@code "S/ 1,234.56"}
     */
    private static String money(String symbol, double value) {
        return String.format(Locale.US, "%s %,.2f", symbol, value);
    }

    /**
     * Formats a percentage rate with two decimals.
     *
     * @param value the rate, already expressed as a percentage
     * @return a string like {@code "18.25"}
     */
    private static String rate(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}
