package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Input payload to update a client. The id is taken from the request path. */
public record UpdateClientResource(String userId, String name, String dni, Double income,
                                   String occupation, String phone, String vehicleId) {
}
