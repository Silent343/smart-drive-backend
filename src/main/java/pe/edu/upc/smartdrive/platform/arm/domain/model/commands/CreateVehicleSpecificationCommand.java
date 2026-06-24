package pe.edu.upc.smartdrive.platform.arm.domain.model.commands;

/** Command to register a vehicle specification. */
public record CreateVehicleSpecificationCommand(String vehicleId, String brand, String model,
                                                Integer year, String transmission) {
}
