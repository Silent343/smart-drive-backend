package pe.edu.upc.smartdrive.platform.arm.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.DeleteVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetAllVehiclesQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.model.queries.GetVehicleByIdQuery;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleCommandService;
import pe.edu.upc.smartdrive.platform.arm.domain.services.VehicleQueryService;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.CreateVehicleResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.UpdateVehicleResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources.VehicleResource;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.CreateVehicleCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.UpdateVehicleCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.arm.interfaces.rest.transform.VehicleResourceFromEntityAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/** Vehicle CRUD endpoints (ARM bounded context), exposed at {@code /vehicles}. */
@RestController
@RequestMapping(value = "/vehicles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Vehicle catalogue management")
public class VehiclesController {

    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;

    public VehiclesController(VehicleCommandService vehicleCommandService, VehicleQueryService vehicleQueryService) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
    }

    @GetMapping
    @Operation(summary = "List vehicles")
    public ResponseEntity<List<VehicleResource>> getAll() {
        var resources = vehicleQueryService.handle(new GetAllVehiclesQuery()).stream()
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a vehicle by id")
    public ResponseEntity<VehicleResource> getById(@PathVariable String id) {
        return vehicleQueryService.handle(new GetVehicleByIdQuery(id))
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a vehicle")
    public ResponseEntity<VehicleResource> create(@RequestBody CreateVehicleResource resource) {
        var command = CreateVehicleCommandFromResourceAssembler.toCommandFromResource(resource);
        return vehicleCommandService.handle(command)
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> new ResponseEntity<>(r, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vehicle")
    public ResponseEntity<VehicleResource> update(@PathVariable String id, @RequestBody UpdateVehicleResource resource) {
        var command = UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(id, resource);
        return vehicleCommandService.handle(command)
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        vehicleCommandService.handle(new DeleteVehicleCommand(id));
        return ResponseEntity.noContent().build();
    }
}
