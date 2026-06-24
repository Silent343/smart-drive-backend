package pe.edu.upc.smartdrive.platform.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.services.UserQueryService;
import pe.edu.upc.smartdrive.platform.iam.domain.repositories.UserRepository;

import java.util.Optional;

/**
 * Default implementation of {@link UserQueryService}.
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email().toLowerCase());
    }
}
