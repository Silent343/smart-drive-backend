package pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.advisor.domain.model.commands.AskAdvisorCommand;
import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.AdvisorLoanFigures;
import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.ChatTurn;
import pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.resources.AskAdvisorResource;

import java.util.List;

/**
 * Builds an {@link AskAdvisorCommand} from the inbound REST resource.
 */
public final class AskAdvisorCommandFromResourceAssembler {

    private AskAdvisorCommandFromResourceAssembler() {
    }

    /**
     * Maps the request resource to a domain command, carrying either the
     * {@code loanId} or the inline simulated figures.
     *
     * @param resource the inbound REST resource
     * @return the domain command
     */
    public static AskAdvisorCommand toCommandFromResource(AskAdvisorResource resource) {
        List<ChatTurn> history = resource.history() == null
                ? List.of()
                : resource.history().stream()
                .map(item -> new ChatTurn(item.role(), item.content()))
                .toList();

        AdvisorLoanFigures inlineFigures = toInlineFigures(resource.figures());

        return new AskAdvisorCommand(
                resource.loanId(), inlineFigures, resource.question(), history);
    }

    /**
     * Maps the optional inline figures resource to its domain value object.
     *
     * @param figures the figures resource, or {@code null}
     * @return the value object, or {@code null} when no figures were sent
     */
    private static AdvisorLoanFigures toInlineFigures(
            AskAdvisorResource.LoanFiguresResource figures) {
        if (figures == null) {
            return null;
        }
        return new AdvisorLoanFigures(
                figures.currencySymbol(),
                figures.vehiclePrice(),
                figures.initialFee(),
                figures.loanAmount(),
                figures.installmentsQty(),
                figures.fixedInstallment(),
                figures.tceaPct(),
                figures.totalInterest(),
                figures.totalInsurance(),
                figures.totalPostage(),
                figures.totalCommission(),
                figures.ctc(),
                figures.npvDebtor(),
                figures.irrDebtorPct());
    }
}
