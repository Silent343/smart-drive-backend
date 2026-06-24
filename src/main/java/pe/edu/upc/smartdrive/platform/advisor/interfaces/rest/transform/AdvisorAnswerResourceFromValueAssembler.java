package pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.advisor.domain.model.valueobjects.AdvisorAnswer;
import pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.resources.AdvisorAnswerResource;

import java.util.UUID;

/**
 * Builds the outbound {@link AdvisorAnswerResource} from the domain answer.
 */
public final class AdvisorAnswerResourceFromValueAssembler {

    private AdvisorAnswerResourceFromValueAssembler() {
    }

    /**
     * Maps the domain answer to a response resource, assigning a generated id.
     *
     * @param answer the domain answer value object
     * @return the REST response resource
     */
    public static AdvisorAnswerResource toResourceFromValue(AdvisorAnswer answer) {
        return new AdvisorAnswerResource(
                UUID.randomUUID().toString(),
                answer.answer(),
                answer.usedFigures());
    }
}
