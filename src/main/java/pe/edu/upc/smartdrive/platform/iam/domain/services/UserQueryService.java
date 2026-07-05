package pe.edu.upc.smartdrive.platform.iam.domain.services;

import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetSellersByCompanyQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByHandleQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query side of the IAM bounded context.
 */
public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByEmailQuery query);
    Optional<User> handle(GetUserByHandleQuery query);
    List<User> handle(GetSellersByCompanyQuery query);
}
