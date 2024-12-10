package com;

import com.dao.PostDao;
import com.dao.UserDao;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.service.UserService;
import com.utils.ConnectionProvider;
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

            // Создание экземпляров DAO и Service
            UserDao userDao = new UserDao(connectionProvider);
            UserService userService = new UserService(userDao);
            PostDao postDao = new PostDao(connectionProvider);

            // Добавление объектов в контекст приложения
            ServletContext context = sce.getServletContext();
            context.setAttribute("userDao", userDao);
            context.setAttribute("userService", userService);
            context.setAttribute("postDao", postDao);

            // Логгирование успешной инициализации
            System.out.println("userDao, userService, and postDao successfully added to ServletContext.");
        } catch (Exception e) {
            System.err.println("Error initializing application context: " + e.getMessage());
            throw new RuntimeException("Error initializing application context", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            // Остановить фоновые потоки MySQL
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (connectionProvider != null) {
            connectionProvider.close();
            System.out.println("Connection pool successfully closed.");
        }
    }
}








