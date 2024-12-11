package com.service;

import com.dao.UserDao;
import com.models.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.utils.PasswordUtils;


public class UserService {
    private final UserDao userDao;

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

    public boolean isNonAnonymous(HttpServletRequest req, HttpServletResponse resp) {
        return req.getSession(false) != null && req.getSession(false).getAttribute("authenticatedUser") != null;
    }

    public User getUser(HttpServletRequest req, HttpServletResponse res) {
        if (req.getSession(false) == null) {
            return null;
        }
        Object userObj = req.getSession(false).getAttribute("authenticatedUser");
        if (userObj instanceof User) {
            return (User) userObj;
        }
        return null;
    }

    public void logout(HttpServletRequest req) {
        if (req.getSession(false) != null) {
            req.getSession(false).invalidate();
        }
    }
}


