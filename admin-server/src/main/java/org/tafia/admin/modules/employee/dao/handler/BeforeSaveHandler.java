package org.tafia.admin.modules.employee.dao.handler;

import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tafia.admin.modules.employee.model.User;

@Component
@RepositoryEventHandler
public class BeforeSaveHandler {

    private PasswordEncoder passwordEncoder;

    public BeforeSaveHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @HandleBeforeSave
    public void handlePersonSave(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
