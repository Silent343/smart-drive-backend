package pe.edu.upc.smartdrive.platform.arm.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehicleCommercialsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleCommercialByIdQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleCommercialCommandService;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleCommercialQueryService;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.CreateVehicleCommercialResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.UpdateVehicleCommercialResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.VehicleCommercialResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.CreateVehicleCommercialCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.UpdateVehicleCommercialCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.VehicleCommercialResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Vehicle commercial CRUD endpoints (ARM bounded context), exposed at
 * {@code /vehicle-commercials}.
 */
@RestController
@RequestMapping(value = "/vehicle-commercials", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Vehicle Commercials", description = "Vehicle pricing and dealer management")
public class VehicleCommercialsController {

    private final VehicleCommercialCommandService commandService;
    private final VehicleCommercialQueryService queryService;

    public VehicleCommercialsController(VehicleCommercialCommandService commandService,
                                        VehicleCommercialQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    @Operation(summary = "List vehicle commercial records")
    public ResponseEntity<List<VehicleCommercialResource>> getAll() {
        var resources = queryService.handle(new GetAllVehicleCommercialsQuery()).stream()
                .map(VehicleCommercialResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a vehicle commercial record by id")
    public ResponseEntity<VehicleCommercialResource> getById(@PathVariable String id) {
        return queryService.handle(new GetVehicleCommercialByIdQuery(id))
                .map(VehicleCommercialResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a vehicle commercial record")
    public ResponseEntity<VehicleCommercialResource> create(@RequestBody CreateVehicleCommercialResource resource) {
        var command = CreateVehicleCommercialCommandFromResourceAssembler.toCommandFromResource(resource);
        return commandService.handle(command)
                .map(VehicleCommercialResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> new ResponseEntity<>(r, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vehicle commercial record")
    public ResponseEntity<VehicleCommercialResource> update(@PathVariable String id,
                                                            @RequestBody UpdateVehicleCommercialResource resource) {
        var command = UpdateVehicleCommercialCommandFromResourceAssembler.toCommandFromResource(id, resource);
        return commandService.handle(command)
                .map(VehicleCommercialResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle commercial record")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        commandService.handle(new DeleteVehicleCommercialCommand(id));
        return ResponseEntity.noContent().build();
    }
}
