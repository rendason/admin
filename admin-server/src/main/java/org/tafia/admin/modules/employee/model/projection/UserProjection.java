package org.tafia.admin.modules.employee.model.projection;

import org.springframework.data.rest.core.config.Projection;
import org.tafia.admin.modules.employee.model.User;

import java.util.Set;

@Projection(name = "userProjection", types = {User.class})
public interface UserProjection {

    String getUsername();

    Set<String> getRoles();

}
