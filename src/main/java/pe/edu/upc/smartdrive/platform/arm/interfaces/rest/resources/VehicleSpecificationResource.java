package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Output representation of a vehicle specification (camelCase). */
public record VehicleSpecificationResource(String id, String vehicleId, String brand, String model,
                                           Integer year, String transmission) {
}
