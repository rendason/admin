package org.tafia.admin;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.tafia.admin.modules.employee.dao.UserDao;
import org.tafia.admin.modules.employee.model.User;

/**
 * 应用入口
 */
@SpringBootApplication
public class Application implements ApplicationContextAware{

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Application.applicationContext = applicationContext;
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    public static User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null) return null;
        UserDao userDao = getContext().getBean(UserDao.class);
        if (userDao == null) return null;
        return userDao.findByUsername(username);
    }
}
