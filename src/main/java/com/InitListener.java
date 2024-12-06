package com;

import com.dao.UserDao;
import com.service.UserService;
import com.utils.ConnectionProvider;
import com.utils.DbException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitListener implements ServletContextListener {
    private ConnectionProvider connectionProvider;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Инициализация ConnectionProvider
            connectionProvider = ConnectionProvider.getInstance();

            // Создание экземпляра UserDao
            UserDao userDao = new UserDao(connectionProvider);

            // Создание экземпляра UserService
            UserService userService = new UserService(userDao);

            // Добавление объектов в контекст приложения
            ServletContext context = sce.getServletContext();
            context.setAttribute("userDao", userDao);
            context.setAttribute("userService", userService);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing application context", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Закрытие ресурсов, включая соединения
        if (connectionProvider != null) {
            connectionProvider.destroy();
        }
    }
}







