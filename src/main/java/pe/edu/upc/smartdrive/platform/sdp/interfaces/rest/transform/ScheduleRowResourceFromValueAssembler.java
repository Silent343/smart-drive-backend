package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.ScheduleRow;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.ScheduleRowResource;

/** Maps a {@link ScheduleRow} value object to its REST resource. */
public class ScheduleRowResourceFromValueAssembler {
    public static ScheduleRowResource toResourceFromValue(ScheduleRow row) {
        return new ScheduleRowResource(
                row.id(),
                row.loanId(),
                row.installmentNo(),
                row.paymentDate(),
                row.openingBalance(),
                row.interest(),
                row.amortization(),
                row.insurance(),
                row.postage(),
                row.commission(),
                row.monthlyPayment(),
                row.endingBalance(),
                row.gracePeriodType());
    }
}
