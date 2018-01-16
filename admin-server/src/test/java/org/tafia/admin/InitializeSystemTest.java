package org.tafia.admin;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.tafia.admin.modules.employee.dao.UserDao;
import org.tafia.admin.modules.employee.model.User;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.Collections;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
@WithMockUser(username = "admin", authorities = "owner")
//@Ignore
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
        user.setRoles(Collections.singleton("owner"));
        user.setSalary(Collections.emptyMap());
        userDao.save(user);
    }

    @Test
    public void testFindByName() {
        User user = userDao.findByUsername("admin");
        System.out.println(JSON.toJSONString(user));
    }

    @Test
    public void testDeleteUsers() {
        userDao.deleteAll();
    }
}
