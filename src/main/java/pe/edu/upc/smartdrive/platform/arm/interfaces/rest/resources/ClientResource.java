package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Output representation of a client (camelCase, matching the frontend model). */
public record ClientResource(String id, String userId, String name, String dni, Double income,
                             String occupation, String phone, String vehicleId) {
}
