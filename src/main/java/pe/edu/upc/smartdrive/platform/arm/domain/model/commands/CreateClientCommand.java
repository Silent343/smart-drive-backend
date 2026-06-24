package pe.edu.upc.smartdrive.platform.arm.domain.model.commands;

/** Command to register a new client. */
public record CreateClientCommand(String userId, String name, String dni, Double income,
                                  String occupation, String phone, String vehicleId) {
}
