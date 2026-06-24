package pe.edu.upc.smartdrive.platform.advisor.domain.services;

import pe.edu.upc.smartdrive.platform.advisor.domain.model.commands.AskAdvisorCommand;
import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.AdvisorAnswer;

/**
 * Command side of the advisor bounded context.
 */
public interface AdvisorCommandService {

    /**
     * Answers a question about a loan, grounding the response in the loan's
     * real financial figures.
     *
     * @param command the question, target loan and conversation history
     * @return the grounded {@link AdvisorAnswer}
     */
    AdvisorAnswer handle(AskAdvisorCommand command);
}
