package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.service.UserService;
import com.utils.DbException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class SigninServlet extends HttpServlet {
    private UserDao userDao;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Получение объектов UserDao и UserService из контекста
        userDao = (UserDao) getServletContext().getAttribute("userDao");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Переадресация на страницу входа
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Получение параметров формы
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            req.setAttribute("message", "Все поля должны быть заполнены.");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
            return;
        }

        try {
            // Получаем пользователя по email
            User user = userDao.getUserByEmail(email);

            // Проверяем корректность пароля
            if (user == null || !userService.verifyPassword(password, user.getPassword())) {
                req.setAttribute("message", "Неверное имя пользователя или пароль.");
                req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
                return;
            }

            // Аутентифицируем пользователя
            userService.auth(user, req, resp);

            // Перенаправление на главную страницу
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (DbException e) {
            throw new ServletException("Ошибка при аутентификации пользователя", e);
        }
    }
}

