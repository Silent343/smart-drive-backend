package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetAllCreditConfigsQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetCreditConfigByIdQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.CreditConfigCommandService;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.CreditConfigQueryService;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.CreditConfigResource;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform.CreateCreditConfigCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform.CreditConfigResourceFromEntityAssembler;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform.UpdateCreditConfigCommandFromResourceAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Credit configuration endpoints (SDP bounded context), exposed at {@code /credit-configs}.
 *
 * <p>Resource fields are snake_case to match the SmartDrive frontend, and collection
 * responses are plain JSON arrays.</p>
 */
@RestController
@RequestMapping(value = "/credit-configs", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Credit Configs", description = "Credit financial parameters")
public class CreditConfigsController {

    private final CreditConfigCommandService creditConfigCommandService;
    private final CreditConfigQueryService creditConfigQueryService;

    public CreditConfigsController(CreditConfigCommandService creditConfigCommandService,
                                   CreditConfigQueryService creditConfigQueryService) {
        this.creditConfigCommandService = creditConfigCommandService;
        this.creditConfigQueryService = creditConfigQueryService;
    }

    @GetMapping
    @Operation(summary = "List credit configurations")
    public ResponseEntity<List<CreditConfigResource>> getAll() {
        var resources = creditConfigQueryService.handle(new GetAllCreditConfigsQuery()).stream()
                .map(CreditConfigResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a credit configuration by id")
    public ResponseEntity<CreditConfigResource> getById(@PathVariable Long id) {
        return creditConfigQueryService.handle(new GetCreditConfigByIdQuery(id))
                .map(CreditConfigResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a credit configuration")
    public ResponseEntity<CreditConfigResource> create(@RequestBody CreditConfigResource resource) {
        var command = CreateCreditConfigCommandFromResourceAssembler.toCommandFromResource(resource);
        return creditConfigCommandService.handle(command)
                .map(CreditConfigResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> new ResponseEntity<>(r, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a credit configuration")
    public ResponseEntity<CreditConfigResource> update(@PathVariable Long id,
                                                       @RequestBody CreditConfigResource resource) {
        var command = UpdateCreditConfigCommandFromResourceAssembler.toCommandFromResource(id, resource);
        return creditConfigCommandService.handle(command)
                .map(CreditConfigResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
