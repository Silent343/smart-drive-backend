package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Input payload to update a vehicle specification. The id comes from the path. */
public record UpdateVehicleSpecificationResource(String vehicleId, String brand, String model,
                                                 Integer year, String transmission) {
}
