package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.service.UserService;
import com.util.DbException;
import com.util.PasswordUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signin")
public class SigninServlet extends HttpServlet {
    private UserDao userDao;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userDao = (UserDao) getServletContext().getAttribute("userDao");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Переадресация на страницу входа
        getServletContext().getRequestDispatcher("/WEB-INF/view/security/signin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username != null && password != null) {
            try {
                // Получаем пользователя по имени и паролю
                User user = userDao.getUserByUsernameAndPassword(username, password);

                if (user == null) {
                    req.setAttribute("message", "Неверное имя пользователя или пароль.");
                    getServletContext().getRequestDispatcher("/WEB-INF/view/security/signin.jsp").forward(req, resp);
                    return;
                }

                // Выполняем аутентификацию пользователя
                userService.auth(user, req, resp);
                resp.sendRedirect(getServletContext().getContextPath() + "/"); // Перенаправление на домашнюю страницу

            } catch (DbException e) {
                throw new ServletException("Ошибка при аутентификации пользователя", e);
            }
        } else {
            req.setAttribute("message", "Все поля должны быть заполнены.");
            getServletContext().getRequestDispatcher("/WEB-INF/view/security/signin.jsp").forward(req, resp);
        }
    }
}
