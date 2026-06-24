package pe.edu.upc.smartdrive.platform.advisor.domain.services;

import java.util.Optional;

/**
 * Outbound port that supplies a loan's figures as a ready-to-ground text
 * block, plus the list of figure labels used.
 *
 * <p>This keeps the advisor context decoupled from the SDP context: the
 * implementation reads the {@code Loan} aggregate and formats it, so the
 * advisor never depends on SDP's internal model.</p>
 */
public interface LoanFiguresProvider {

    /**
     * Loads and formats a loan's figures for grounding.
     *
     * @param loanId the id of the loan
     * @return the grounding text and figure labels, or empty if not found
     */
    Optional<LoanFigures> figuresFor(Long loanId);

    /**
     * The formatted figures of a loan.
     *
     * @param groundingText a human-readable block listing every figure
     * @param figureLabels  short labels of the figures (for UI transparency)
     */
    record LoanFigures(String groundingText, java.util.List<String> figureLabels) {
    }
}
