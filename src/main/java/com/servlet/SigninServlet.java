package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.service.UserService;
import com.utils.DbException;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import javax.servlet.ServletContext;

@WebServlet("/login")
public class SigninServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        UserService userService = (UserService) context.getAttribute("userService");

        if (userDao == null || userService == null) {
            throw new ServletException("userDao or userService are null in context.");
        }

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            req.setAttribute("message", "Все поля должны быть заполнены.");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userDao.getUserByEmail(email);

            if (user == null || !userService.verifyPassword(password, user.getPassword())) {
                req.setAttribute("message", "Неверное имя пользователя или пароль.");
                req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setMaxInactiveInterval(24 * 60 * 60);

            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (DbException e) {
            throw new ServletException("Ошибка при аутентификации пользователя", e);
        }
    }

}



