package pe.edu.upc.smartdrive.platform.sdp.domain.services;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetAllCreditConfigsQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetCreditConfigByIdQuery;

import java.util.List;
import java.util.Optional;

/** Query side of the CreditConfig aggregate. */
public interface CreditConfigQueryService {
    List<CreditConfig> handle(GetAllCreditConfigsQuery query);
    Optional<CreditConfig> handle(GetCreditConfigByIdQuery query);
}
