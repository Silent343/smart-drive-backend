package pe.edu.upc.smartdrive.platform.sdp.domain.services;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanByIdQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanReportQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanScheduleQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.LoanReportData;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.ScheduleRow;

import java.util.List;
import java.util.Optional;

/** Query side of the Loan aggregate. */
public interface LoanQueryService {
    Optional<Loan> handle(GetLoanByIdQuery query);
    List<ScheduleRow> handle(GetLoanScheduleQuery query);
    Optional<LoanReportData> handle(GetLoanReportQuery query);
}
