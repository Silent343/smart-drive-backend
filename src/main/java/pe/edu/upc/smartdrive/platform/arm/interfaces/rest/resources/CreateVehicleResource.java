package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Input payload to create a vehicle. The id is assigned by the server. */
public record CreateVehicleResource(String code, String status, String imageUrl) {
}
