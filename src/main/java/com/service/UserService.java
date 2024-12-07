package com.service;

import com.dao.UserDao;
import com.models.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.utils.PasswordUtils;


public class UserService {
    private final UserDao userDao;

    // Конструктор принимает UserDao
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
    public boolean verifyPassword(String inputPassword, String storedHashedPassword) {
        try {
            return PasswordUtils.verifyPassword(inputPassword, storedHashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке пароля", e);
        }
    }

    public void auth(User user, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("authenticatedUser", user);
    }


    // Проверка, аутентифицирован ли пользователь
    public boolean isNonAnonymous(HttpServletRequest req, HttpServletResponse resp) {
        return req.getSession(false) != null && req.getSession(false).getAttribute("authenticatedUser") != null;
    }

    // Получение текущего пользователя из сессии
    public User getUser(HttpServletRequest req, HttpServletResponse res) {
        if (req.getSession(false) == null) {
            return null; // Если сессия отсутствует
        }
        Object userObj = req.getSession(false).getAttribute("authenticatedUser");
        if (userObj instanceof User) {
            return (User) userObj;
        }
        return null; // Если объект отсутствует или имеет неправильный тип
    }

    public void logout(HttpServletRequest req) {
        if (req.getSession(false) != null) {
            req.getSession(false).invalidate(); // Уничтожение сессии
        }
    }
}


