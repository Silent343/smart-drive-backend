package pe.edu.upc.smartdrive.platform.advisor.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.advisor.domain.model.commands.AskAdvisorCommand;
import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.AdvisorAnswer;
import pe.edu.upc.smartdrive.platform.advisor.domain.services.AdvisorCommandService;
import pe.edu.upc.smartdrive.platform.advisor.domain.services.LanguageModelPort;
import pe.edu.upc.smartdrive.platform.advisor.domain.services.LoanFiguresProvider;
import pe.edu.upc.smartdrive.platform.advisor.domain.services.LoanFiguresProvider.LoanFigures;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the advisor command side.
 *
 * <p>Orchestrates the flow: resolve the loan's real figures, build a grounded
 * prompt, and delegate generation to the language model port. The figures are
 * the single source of truth, which is what stops the model from inventing
 * numbers.</p>
 *
 * <p>Figures come from one of two places: a confirmed loan loaded by its id, or
 * — during a simulation, when no {@code loanId} exists yet — the inline figures
 * carried in the request. Either grounds the answer equally well.</p>
 */
@Service
public class AdvisorCommandServiceImpl implements AdvisorCommandService {

    /**
     * System instruction: defines the assistant's persona and the hard rule to
     * answer only from the supplied loan figures.
     */
    private static final String SYSTEM_INSTRUCTION = """
            Eres un asesor financiero de SmartDrive que ayuda a clientes a \
            entender su crédito vehicular. Respondes en español, de forma clara \
            y breve, evitando jerga innecesaria.

            Reglas estrictas:
            - Responde ÚNICAMENTE con base en las cifras del crédito que se te \
            entregan como contexto. No inventes números.
            - Si la pregunta no se puede responder con esas cifras, dilo con \
            honestidad y sugiere con quién consultarlo.
            - Cuando expliques un término (TCEA, TIR, VAN, seguro de \
            desgravamen), hazlo en lenguaje sencillo.
            - No des asesoría de inversión ni promesas; explica lo que los \
            números del crédito significan.""";

    private final LoanFiguresProvider loanFiguresProvider;
    private final LanguageModelPort languageModel;

    /**
     * @param loanFiguresProvider supplies a confirmed loan's grounded figures
     * @param languageModel       the LLM port used to generate the answer
     */
    public AdvisorCommandServiceImpl(
            LoanFiguresProvider loanFiguresProvider,
            LanguageModelPort languageModel) {
        this.loanFiguresProvider = loanFiguresProvider;
        this.languageModel = languageModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdvisorAnswer handle(AskAdvisorCommand command) {
        Optional<LoanFigures> figures = resolveFigures(command);

        if (figures.isEmpty()) {
            return new AdvisorAnswer(
                    "No tengo las cifras del crédito para responder. Ejecuta la "
                            + "simulación o confirma el préstamo e inténtalo de nuevo.",
                    List.of());
        }

        LoanFigures loanFigures = figures.get();
        String answer = languageModel.generate(
                SYSTEM_INSTRUCTION,
                loanFigures.groundingText(),
                command.history(),
                command.question());

        return new AdvisorAnswer(answer, loanFigures.figureLabels());
    }

    /**
     * Resolves the figures to ground on: a confirmed loan (by id) when it
     * exists, otherwise the inline simulated figures carried in the request.
     *
     * @param command the incoming command
     * @return the figures, or empty when neither source is available
     */
    private Optional<LoanFigures> resolveFigures(AskAdvisorCommand command) {
        if (command.loanId() != null) {
            Optional<LoanFigures> persisted =
                    loanFiguresProvider.figuresFor(command.loanId());
            if (persisted.isPresent()) {
                return persisted;
            }
        }
        if (command.inlineFigures() != null) {
            return Optional.of(command.inlineFigures().toLoanFigures());
        }
        return Optional.empty();
    }
}
