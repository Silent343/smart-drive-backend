package pe.edu.upc.smartdrive.platform.advisor.domain.model.commands;

import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.AdvisorLoanFigures;
import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.ChatTurn;

import java.util.List;

/**
 * Command to ask the advisor a question about a loan.
 *
 * <p>The loan may be referenced in one of two ways:</p>
 * <ul>
 *   <li>by {@code loanId}, for a confirmed (persisted) loan whose figures are
 *       loaded from the SDP context; or</li>
 *   <li>by {@code inlineFigures}, for a simulated loan that has not been saved
 *       yet (so it has no id), whose figures travel with the request.</li>
 * </ul>
 *
 * <p>At least one of the two must be present. When both are present the
 * persisted loan wins, falling back to the inline figures only if the id
 * resolves to nothing.</p>
 *
 * @param loanId        the id of the confirmed loan, or {@code null} in
 *                      simulation
 * @param inlineFigures the simulated loan's figures, or {@code null} when a
 *                      {@code loanId} is given
 * @param question      the user's natural-language question
 * @param history       prior conversation turns for context (may be empty)
 */
public record AskAdvisorCommand(
        Long loanId,
        AdvisorLoanFigures inlineFigures,
        String question,
        List<ChatTurn> history) {
}
