package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Input payload to create a vehicle specification. */
public record CreateVehicleSpecificationResource(String vehicleId, String brand, String model,
                                                 Integer year, String transmission) {
}
