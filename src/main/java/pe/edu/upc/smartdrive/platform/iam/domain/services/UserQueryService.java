package pe.edu.upc.smartdrive.platform.iam.domain.services;

import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.Optional;

/**
 * Query side of the IAM bounded context.
 */
public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByEmailQuery query);
}
