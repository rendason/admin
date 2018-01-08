package org.tafia.admin.modules.common.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.tafia.admin.modules.common.model.User;
import org.tafia.admin.modules.common.model.projection.UserProjection;

@Repository
@RepositoryRestResource(excerptProjection = UserProjection.class)
@PreAuthorize("hasRole('admin')")
public interface UserDao extends MongoRepository<User, String> {

    User findByUsername(String name);
}
