package org.tafia.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.tafia.admin.modules.common.dao.UserDao;
import org.tafia.admin.modules.common.model.User;
import org.testng.annotations.Test;

import java.util.Collections;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
public class InitializeSystemTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("tafia.org"));
        user.setRoles(Collections.singleton("admin"));
        userDao.save(user);
    }
}
