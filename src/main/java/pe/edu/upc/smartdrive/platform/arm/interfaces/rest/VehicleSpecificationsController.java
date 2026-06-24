package pe.edu.upc.smartdrive.platform.arm.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehicleSpecificationsQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleSpecificationByIdQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleSpecificationCommandService;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleSpecificationQueryService;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.CreateVehicleSpecificationResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.UpdateVehicleSpecificationResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.VehicleSpecificationResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.CreateVehicleSpecificationCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.UpdateVehicleSpecificationCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.VehicleSpecificationResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Vehicle specification CRUD endpoints (ARM bounded context), exposed at
 * {@code /vehicle-specifications}.
 */
@RestController
@RequestMapping(value = "/vehicle-specifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Vehicle Specifications", description = "Vehicle technical details management")
public class VehicleSpecificationsController {

    private final VehicleSpecificationCommandService commandService;
    private final VehicleSpecificationQueryService queryService;

    public VehicleSpecificationsController(VehicleSpecificationCommandService commandService,
                                           VehicleSpecificationQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    @Operation(summary = "List vehicle specifications")
    public ResponseEntity<List<VehicleSpecificationResource>> getAll() {
        var resources = queryService.handle(new GetAllVehicleSpecificationsQuery()).stream()
                .map(VehicleSpecificationResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a vehicle specification by id")
    public ResponseEntity<VehicleSpecificationResource> getById(@PathVariable String id) {
        return queryService.handle(new GetVehicleSpecificationByIdQuery(id))
                .map(VehicleSpecificationResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a vehicle specification")
    public ResponseEntity<VehicleSpecificationResource> create(@RequestBody CreateVehicleSpecificationResource resource) {
        var command = CreateVehicleSpecificationCommandFromResourceAssembler.toCommandFromResource(resource);
        return commandService.handle(command)
                .map(VehicleSpecificationResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> new ResponseEntity<>(r, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vehicle specification")
    public ResponseEntity<VehicleSpecificationResource> update(@PathVariable String id,
                                                               @RequestBody UpdateVehicleSpecificationResource resource) {
        var command = UpdateVehicleSpecificationCommandFromResourceAssembler.toCommandFromResource(id, resource);
        return commandService.handle(command)
                .map(VehicleSpecificationResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle specification")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        commandService.handle(new DeleteVehicleSpecificationCommand(id));
        return ResponseEntity.noContent().build();
    }
}
