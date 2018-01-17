package org.tafia.admin.modules.employee.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.tafia.admin.modules.employee.model.User;
import org.tafia.admin.modules.employee.model.projection.UserProjection;

@Repository
@RepositoryRestResource(excerptProjection = UserProjection.class)
@PreAuthorize("hasAnyAuthority('owner', 'manager')")
public interface UserDao extends MongoRepository<User, String> {

    @PreAuthorize("isAnonymous()")
    User findByUsername(String name);

    @PreAuthorize("isAnonymous()")
    User findUserByWeixin(String weixin);

}
