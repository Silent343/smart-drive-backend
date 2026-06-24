package pe.edu.upc.smartdrive.platform.arm.domain.model.commands;

/** Command to update a vehicle specification. */
public record UpdateVehicleSpecificationCommand(String id, String vehicleId, String brand, String model,
                                                Integer year, String transmission) {
}
