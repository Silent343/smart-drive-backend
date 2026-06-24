package pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects;

import java.util.List;

/**
 * The advisor's answer to a question, grounded in a loan's real figures.
 *
 * @param answer      the natural-language answer
 * @param usedFigures human-readable labels of the loan figures that informed
 *                    the answer (shown to the user as transparency)
 */
public record AdvisorAnswer(String answer, List<String> usedFigures) {
}
