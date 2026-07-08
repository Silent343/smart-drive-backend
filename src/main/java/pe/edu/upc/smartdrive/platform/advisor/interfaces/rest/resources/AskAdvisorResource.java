package pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request body for {@code POST /advisor/ask}. Field names are snake_case to
 * match the frontend assembler, via explicit {@link JsonProperty} mappings.
 *
 * <p>Either {@code loanId} (a confirmed loan) or {@code figures} (a simulated,
 * not-yet-saved loan) must be provided so the advisor has real numbers to
 * ground on.</p>
 *
 * @param loanId   the id of the confirmed loan the question is about, or
 *                 {@code null} when asking about a simulation
 * @param figures  the simulated loan's figures, sent inline when there is no
 *                 {@code loanId} yet
 * @param question the user's question
 * @param history  prior conversation turns
 */
public record AskAdvisorResource(
        @JsonProperty("loan_id") Long loanId,
        @JsonProperty("figures") LoanFiguresResource figures,
        @JsonProperty("question") String question,
        @JsonProperty("history") List<HistoryItemResource> history) {

    /**
     * A prior conversation turn.
     *
     * @param role    "user" or "assistant"
     * @param content the message text
     */
    public record HistoryItemResource(
            @JsonProperty("role") String role,
            @JsonProperty("content") String content) {
    }

    /**
     * The inline figures of a simulated loan.
     *
     * <p>Amounts are in the loan's currency; {@code tceaPct} and
     * {@code irrDebtorPct} are already percentages (e.g. {@code 18.25}).</p>
     *
     * @param currencySymbol   the currency symbol for amounts (e.g. "S/")
     * @param vehiclePrice     the vehicle price
     * @param initialFee       the down payment
     * @param loanAmount       the financed amount
     * @param installmentsQty  the number of installments
     * @param fixedInstallment the fixed monthly installment
     * @param tceaPct          the TCEA as a percentage
     * @param totalInterest    the total interest
     * @param totalInsurance   the total credit-life insurance
     * @param totalPostage     the total postage
     * @param totalCommission  the total commissions
     * @param ctc              the total cost of credit
     * @param npvDebtor        the debtor's NPV
     * @param irrDebtorPct     the debtor's IRR as a percentage
     */
    public record LoanFiguresResource(
            @JsonProperty("currency_symbol") String currencySymbol,
            @JsonProperty("vehicle_price") double vehiclePrice,
            @JsonProperty("initial_fee") double initialFee,
            @JsonProperty("loan_amount") double loanAmount,
            @JsonProperty("installments_qty") int installmentsQty,
            @JsonProperty("fixed_installment") double fixedInstallment,
            @JsonProperty("tcea_pct") double tceaPct,
            @JsonProperty("total_interest") double totalInterest,
            @JsonProperty("total_insurance") double totalInsurance,
            @JsonProperty("total_postage") double totalPostage,
            @JsonProperty("total_commission") double totalCommission,
            @JsonProperty("ctc") double ctc,
            @JsonProperty("npv_debtor") double npvDebtor,
            @JsonProperty("irr_debtor_pct") double irrDebtorPct) {
    }
}
