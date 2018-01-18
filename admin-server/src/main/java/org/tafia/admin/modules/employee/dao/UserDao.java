package org.tafia.admin.modules.employee.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.tafia.admin.modules.employee.model.User;
import org.tafia.admin.modules.employee.model.projection.UserProjection;

@Repository
@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface UserDao extends MongoRepository<User, String> {

    User findByUsername(String name);

    User findUserByWeixin(String weixin);

}
