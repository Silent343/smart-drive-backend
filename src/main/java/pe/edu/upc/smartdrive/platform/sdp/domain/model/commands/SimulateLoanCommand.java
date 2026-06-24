package pe.edu.upc.smartdrive.platform.sdp.domain.model.commands;

import java.time.Instant;

/** Command to simulate a loan (compute indicators and schedule) without persisting it. */
public record SimulateLoanCommand(String carId, String clientId, Long configId, double initialFee,
                                  double vehiclePrice, double loanAmount, int installmentsQty, Instant startDate) {
}
