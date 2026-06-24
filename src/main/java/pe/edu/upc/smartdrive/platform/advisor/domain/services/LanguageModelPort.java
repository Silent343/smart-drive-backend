package pe.edu.upc.smartdrive.platform.advisor.domain.services;

import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.ChatTurn;

import java.util.List;

/**
 * Outbound port to a large language model.
 *
 * <p>Defined in the domain so the application orchestration depends on this
 * abstraction, not on a concrete provider. The Gemini implementation lives in
 * the infrastructure layer (Dependency Inversion).</p>
 */
public interface LanguageModelPort {

    /**
     * Generates an answer given a system instruction, the grounded context,
     * the prior turns and the new question.
     *
     * @param systemInstruction high-level instruction (rules, persona)
     * @param groundingContext  the loan figures the answer must be based on
     * @param history           prior conversation turns
     * @param question          the user's new question
     * @return the model's generated answer text
     */
    String generate(
            String systemInstruction,
            String groundingContext,
            List<ChatTurn> history,
            String question);
}
