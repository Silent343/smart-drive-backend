package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Input payload to update a vehicle. The id is taken from the request path. */
public record UpdateVehicleResource(String code, String status, String imageUrl) {
}
