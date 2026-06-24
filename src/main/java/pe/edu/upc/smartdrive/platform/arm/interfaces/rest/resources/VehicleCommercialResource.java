package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Output representation of vehicle commercial information (camelCase). */
public record VehicleCommercialResource(String id, String vehicleId, String userId, Double price, String company) {
}
