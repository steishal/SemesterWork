package com.servlet;
import com.dao.UserDao;
import com.models.User;
import com.service.UserService;



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
import java.security.NoSuchAlgorithmException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
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
        // Направляем пользователя на страницу регистрации
        req.getRequestDispatcher("register.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Получение параметров формы
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phone");
        String vkLink = req.getParameter("vkLink");
        String tgLink = req.getParameter("tgLink");

        // Валидация данных
        if (username == null || password == null || email == null || phoneNumber == null ||
                username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            req.setAttribute("error", "Все обязательные поля должны быть заполнены.");
            req.getRequestDispatcher("register.html").forward(req, resp);
            return;
        }

        try {
            // Проверка на существование пользователя с таким именем
            if (userDao.getUserByUsername(username) != null) {
                req.setAttribute("error", "Пользователь с таким именем уже существует.");
                req.getRequestDispatcher("register.html").forward(req, resp);
                return;
            }

            // Хеширование пароля перед сохранением
            String hashedPassword = PasswordUtils.hashPassword(password);

            // Создание объекта User
            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword); // Сохраняем захешированный пароль
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setVkLink(vkLink);
            user.setTgLink(tgLink);

            // Сохранение пользователя в базе данных
            userDao.saveUser(user);
            resp.sendRedirect("success.html"); // Перенаправление на страницу успеха

        } catch (DbException | NoSuchAlgorithmException e) {
            req.setAttribute("error", "Ошибка при сохранении пользователя: " + e.getMessage());
            req.getRequestDispatcher("register.html").forward(req, resp);
        }
    }

}

