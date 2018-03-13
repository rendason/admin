package org.tafia.admin.modules.common.dao.handler;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tafia.admin.modules.common.util.RequestUtils;
import org.tafia.admin.modules.employee.model.AuthorizedAddress;
import org.tafia.admin.modules.employee.model.User;

@Component
@org.springframework.data.rest.core.annotation.RepositoryEventHandler
public class RepositoryEventHandler {

    private PasswordEncoder passwordEncoder;

    public RepositoryEventHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @HandleBeforeSave
    @HandleBeforeCreate
    public void beforeUserSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @HandleBeforeCreate
    public void beforeAuthorizedAddressSave(AuthorizedAddress authorizedAddress) {
        authorizedAddress.setIp(RequestUtils.getIp());
    }
}
