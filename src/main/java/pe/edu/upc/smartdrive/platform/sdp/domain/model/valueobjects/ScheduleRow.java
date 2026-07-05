package pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects;

import java.time.Instant;

/**
 * A single row of the amortization schedule. Derived value object: not persisted, but
 * regenerated on demand by the calculation engine. Besides the core French-method columns
 * it breaks down every recurring cost (desgravamen and all-risk insurance, GPS, postage,
 * administration commission and the IGV/ITF tax) so the schedule mirrors the Interbank
 * spreadsheet column by column.
 */
public record ScheduleRow(String id, String loanId, int installmentNo, Instant paymentDate,
                          double openingBalance, double interest, double amortization,
                          double insurance,        // desgravamen
                          double riskInsurance,    // seguro contra todo riesgo
                          double gps,
                          double postage,
                          double commission,       // administration
                          double tax,              // IGV/ITF over fees & insurance
                          double monthlyPayment, double endingBalance,
                          String gracePeriodType) {
}
