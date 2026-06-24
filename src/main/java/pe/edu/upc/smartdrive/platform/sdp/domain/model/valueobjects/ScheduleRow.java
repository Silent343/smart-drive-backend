package pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects;

import java.time.Instant;

/**
 * A single row of the amortization schedule. This is a derived value object: it is not
 * persisted, but regenerated on demand by the calculation engine.
 */
public record ScheduleRow(String id, String loanId, int installmentNo, Instant paymentDate,
                          double openingBalance, double interest, double amortization, double insurance,
                          double postage, double commission, double monthlyPayment, double endingBalance,
                          String gracePeriodType) {
}
