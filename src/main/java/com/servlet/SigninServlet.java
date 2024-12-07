package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.service.UserService;
import com.utils.DbException;
import javax.servlet.http.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

            // Аутентификация через сессию
            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId());  // Сохраняем ID пользователя в сессии
            session.setAttribute("username", user.getUsername());  // Сохраняем имя пользователя в сессии
            session.setMaxInactiveInterval(30 * 60);  // Время жизни сессии (30 минут)

            // Создаем куку для запоминания пользователя
            Cookie userCookie = new Cookie("user", user.getUsername());
            userCookie.setMaxAge(60 * 60 * 24 * 30);  // Срок действия куки (30 дней)
            userCookie.setHttpOnly(true);  // Защита от JavaScript доступа
            resp.addCookie(userCookie);

            // Перенаправление на профиль
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (DbException e) {
            throw new ServletException("Ошибка при аутентификации пользователя", e);
        }
    }
}



