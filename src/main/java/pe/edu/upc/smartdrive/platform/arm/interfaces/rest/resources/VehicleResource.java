package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Output representation of a vehicle (camelCase). */
public record VehicleResource(String id, String code, String status, String imageUrl) {
}
