package com.service;

import com.dao.UserDao;
import com.models.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.utils.PasswordUtils;

import java.util.Set;


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

    public User authenticate(String email, String password) {
        try {
            User user = userDao.getUserByEmail(email);
            if (user != null && verifyPassword(password, user.getPassword())) {
                return user;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при аутентификации пользователя", e);
        }
    }

    public void auth(User user, HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());
        session.setMaxInactiveInterval(24 * 60 * 60);
    }

    public boolean isNonAnonymous(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }

    public User getUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return null;
        }

        Object userIdObj = session.getAttribute("userId");
        if (userIdObj instanceof Integer) {
            try {
                return userDao.getUserById((Integer) userIdObj);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при получении пользователя из базы данных", e);
            }
        }
        return null;
    }

    public void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isAdmin(HttpServletRequest req) {
        User user = getUser(req);
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }
}


