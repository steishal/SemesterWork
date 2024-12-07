package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.utils.DbException;
import com.utils.PasswordUtils;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Получаем объект UserDao из контекста
        userDao = (UserDao) getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Считываем параметры формы
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String phoneNumber = req.getParameter("phone");
        String vkLink = req.getParameter("vkLink");
        String tgLink = req.getParameter("tgLink");

        // Валидация обязательных полей
        if (username == null || password == null || email == null || phoneNumber == null ||
                username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            req.setAttribute("error", "Все обязательные поля должны быть заполнены.");
            req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
            return;
        }

        try {
            // Проверка, существует ли пользователь с таким именем
            if (userDao.getUserByUsername(username) != null) {
                req.setAttribute("error", "Пользователь с таким именем уже существует.");
                req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
                return;
            }

            // Хешируем пароль перед сохранением
            String hashedPassword = PasswordUtils.hashPassword(password);

            // Создаём объект пользователя
            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword); // Сохраняем хешированный пароль
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setVkLink(vkLink);
            user.setTgLink(tgLink);

            // Сохраняем пользователя в базе данных
            userDao.saveUser(user);

            // Не создаем сессию здесь, пусть пользователь сделает это при входе
            // Создаем куку с именем пользователя для автозаполнения при следующем визите
            Cookie userCookie = new Cookie("username", username);  // Создаем куку с именем пользователя
            userCookie.setMaxAge(60 * 60 * 24);  // Устанавливаем срок действия куки (1 день)
            resp.addCookie(userCookie);  // Добавляем куку в ответ

            // Перенаправление на страницу входа
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (DbException | NoSuchAlgorithmException e) {
            req.setAttribute("error", "Ошибка при сохранении пользователя: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
        }
    }
}



