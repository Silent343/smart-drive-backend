package pe.edu.upc.smartdrive.platform.advisor.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.smartdrive.platform.advisor.domain.services.AdvisorCommandService;
import pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.resources.AdvisorAnswerResource;
import pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.resources.AskAdvisorResource;
import pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.transform.AdvisorAnswerResourceFromValueAssembler;
import pe.edu.upc.smartdrive.platform.advisor.interfaces.rest.transform.AskAdvisorCommandFromResourceAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Advisor endpoints (Advisor bounded context), exposed at {@code /advisor}.
 *
 * <p>The advisor answers natural-language questions about a specific loan,
 * grounding every answer in that loan's real financial figures.</p>
 */
@RestController
@RequestMapping(value = "/advisor", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Advisor", description = "AI assistant that answers questions about a loan")
public class AdvisorController {

    private final AdvisorCommandService advisorCommandService;

    /**
     * @param advisorCommandService the advisor command service
     */
    public AdvisorController(AdvisorCommandService advisorCommandService) {
        this.advisorCommandService = advisorCommandService;
    }

    /**
     * Answers a question about a loan.
     *
     * @param resource the question, target loan id and conversation history
     * @return {@code 200 OK} with the grounded answer
     */
    @PostMapping("/ask")
    @Operation(summary = "Ask the AI advisor a question about a loan")
    public ResponseEntity<AdvisorAnswerResource> ask(@RequestBody AskAdvisorResource resource) {
        var command = AskAdvisorCommandFromResourceAssembler.toCommandFromResource(resource);
        var answer = advisorCommandService.handle(command);
        var body = AdvisorAnswerResourceFromValueAssembler.toResourceFromValue(answer);
        return ResponseEntity.ok(body);
    }
}
