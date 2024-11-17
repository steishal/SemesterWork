package com;

import com.dao.UserDao;
import com.service.UserService;
import com.util.ConnectionProvider;
import com.util.DbException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Получение экземпляра ConnectionProvider
            ConnectionProvider connectionProvider = ConnectionProvider.getInstance();

            // Создание экземпляра UserDao с передачей ConnectionProvider
            UserDao userDao = new UserDao(connectionProvider);

            // Создание экземпляра UserService с передачей UserDao
            UserService userService = new UserService(userDao);

            // Установка объектов в контекст приложения
            sce.getServletContext().setAttribute("userDao", userDao);
            sce.getServletContext().setAttribute("userService", userService);
        } catch (DbException e) {
            throw new RuntimeException("Error initializing context", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Очистка ресурсов, если необходимо
    }
}






