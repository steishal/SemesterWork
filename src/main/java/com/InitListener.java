package com;

import com.dao.*;
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

            connectionProvider = ConnectionProvider.getInstance();

            UserDao userDao = new UserDao(connectionProvider);
            UserService userService = new UserService(userDao);
            PostDao postDao = new PostDao(connectionProvider);
            CommentDao commentDao = new CommentDao(connectionProvider);
            LikeDao likeDao = new LikeDao(connectionProvider);
            CategoryDao categoryDao = new CategoryDao(connectionProvider);

            ServletContext context = sce.getServletContext();
            context.setAttribute("userDao", userDao);
            context.setAttribute("likeDao", likeDao);
            context.setAttribute("commentDao", commentDao);
            context.setAttribute("userService", userService);
            context.setAttribute("postDao", postDao);
            context.setAttribute("categoryDao", categoryDao);
        } catch (Exception e) {
            System.err.println("Error initializing application context: " + e.getMessage());
            throw new RuntimeException("Error initializing application context", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
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








