package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/**
 * Input payload to create a client. Any {@code id} sent by the client is ignored;
 * the server assigns the identifier.
 */
public record CreateClientResource(String userId, String name, String dni, Double income,
                                   String occupation, String phone, String vehicleId) {
}
