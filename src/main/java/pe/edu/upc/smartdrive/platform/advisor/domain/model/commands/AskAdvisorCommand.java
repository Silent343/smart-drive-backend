package pe.edu.upc.smartdrive.platform.advisor.domain.model.commands;

import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.ChatTurn;

import java.util.List;

/**
 * Command to ask the advisor a question about a specific loan.
 *
 * @param loanId   the id of the loan the question is about
 * @param question the user's natural-language question
 * @param history  prior conversation turns for context (may be empty)
 */
public record AskAdvisorCommand(Long loanId, String question, List<ChatTurn> history) {
}
