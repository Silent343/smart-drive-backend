package pe.edu.upc.smartdrive.platform.arm.domain.model.commands;

/** Command to update an existing client identified by {@code id}. */
public record UpdateClientCommand(String id, String userId, String name, String dni, Double income,
                                  String occupation, String phone, String vehicleId) {
}
