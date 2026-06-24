package pe.edu.upc.smartdrive.platform.arm.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllClientsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetClientByIdQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.services.ClientCommandService;
import pe.edu.upc.smartdrive.platform.arm.domain.services.ClientQueryService;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.ClientResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.CreateClientResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.UpdateClientResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.ClientResourceFromEntityAssembler;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.CreateClientCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.UpdateClientCommandFromResourceAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Client CRUD endpoints (ARM bounded context), exposed at {@code /clients}.
 *
 * <p>Collection responses are plain JSON arrays, as expected by the frontend's
 * generic resource service.</p>
 */
@RestController
@RequestMapping(value = "/clients", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Clients", description = "Credit applicant management")
public class ClientsController {

    private final ClientCommandService clientCommandService;
    private final ClientQueryService clientQueryService;

    public ClientsController(ClientCommandService clientCommandService, ClientQueryService clientQueryService) {
        this.clientCommandService = clientCommandService;
        this.clientQueryService = clientQueryService;
    }

    @GetMapping
    @Operation(summary = "List clients")
    public ResponseEntity<List<ClientResource>> getAll() {
        var resources = clientQueryService.handle(new GetAllClientsQuery()).stream()
                .map(ClientResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a client by id")
    public ResponseEntity<ClientResource> getById(@PathVariable String id) {
        return clientQueryService.handle(new GetClientByIdQuery(id))
                .map(ClientResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a client")
    public ResponseEntity<ClientResource> create(@RequestBody CreateClientResource resource) {
        var command = CreateClientCommandFromResourceAssembler.toCommandFromResource(resource);
        return clientCommandService.handle(command)
                .map(ClientResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> new ResponseEntity<>(r, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client")
    public ResponseEntity<ClientResource> update(@PathVariable String id, @RequestBody UpdateClientResource resource) {
        var command = UpdateClientCommandFromResourceAssembler.toCommandFromResource(id, resource);
        return clientCommandService.handle(command)
                .map(ClientResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        clientCommandService.handle(new DeleteClientCommand(id));
        return ResponseEntity.noContent().build();
    }
}
