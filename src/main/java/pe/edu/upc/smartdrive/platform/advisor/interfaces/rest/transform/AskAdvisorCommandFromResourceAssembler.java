package pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.advisor.domain.model.commands.AskAdvisorCommand;
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
     * Maps the request resource to a domain command.
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
        return new AskAdvisorCommand(resource.loanId(), resource.question(), history);
    }
}
