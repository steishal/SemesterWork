package com.service;

import com.dao.UserDao;
import com.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserService {
    private UserDao userDao;

    // Конструктор с параметром UserDao
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // Метод для аутентификации пользователя
    public void auth(User user, HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("user", user);
    }

    // Проверка, является ли пользователь аутентифицированным
    public boolean isNonAnonymous(HttpServletRequest req, HttpServletResponse resp) {
        // Проверка на наличие активной сессии и объекта пользователя
        return req.getSession(false) != null && req.getSession(false).getAttribute("user") != null;
    }

    // Получение текущего пользователя из сессии
    public User getUser(HttpServletRequest req, HttpServletResponse res) {
        if (req.getSession(false) == null) {
            return null; // Если сессии нет, возвращаем null
        }
        Object userObj = req.getSession(false).getAttribute("user");
        if (userObj instanceof User) {
            return (User) userObj; // Если объект существует и это User, возвращаем его
        }
        return null; // Если объект отсутствует или неверного типа, возвращаем null
    }

}

