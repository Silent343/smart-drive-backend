package pe.edu.upc.smartdrive.platform.sdp.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetAllCreditConfigsQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetCreditConfigByIdQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.CreditConfigQueryService;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.CreditConfigRepository;

import java.util.List;
import java.util.Optional;

/** Default {@link CreditConfigQueryService} backed by JPA persistence. */
@Service
public class CreditConfigQueryServiceImpl implements CreditConfigQueryService {

    private final CreditConfigRepository creditConfigRepository;

    public CreditConfigQueryServiceImpl(CreditConfigRepository creditConfigRepository) {
        this.creditConfigRepository = creditConfigRepository;
    }

    @Override
    public List<CreditConfig> handle(GetAllCreditConfigsQuery query) {
        return creditConfigRepository.findAll();
    }

    @Override
    public Optional<CreditConfig> handle(GetCreditConfigByIdQuery query) {
        return creditConfigRepository.findById(query.id());
    }
}
