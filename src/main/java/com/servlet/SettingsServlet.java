package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.utils.DbException;
import com.utils.PasswordUtils;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@WebServlet("/settings")
public class SettingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        User currentUser;
        try {
            currentUser = userDao.getUserById(userId);
        } catch (DbException e) {
            throw new RuntimeException("Ошибка получения данных пользователя", e);
        }

        if (currentUser != null && Boolean.TRUE.equals(session.getAttribute("passwordVerified"))) {
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/WEB-INF/settings.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/confirmPassword");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        User currentUser;
        try {
            currentUser = userDao.getUserById(userId);
        } catch (DbException e) {
            throw new RuntimeException("Ошибка получения данных пользователя", e);
        }

        String action = request.getParameter("action");
        try {
            switch (action) {
                case "updateEmail":
                    String newEmail = request.getParameter("email");
                    if (newEmail != null && newEmail.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                        currentUser.setEmail(newEmail);
                        userDao.updateUser(currentUser);
                    }
                    break;

                case "updatePassword":
                    String newPassword = request.getParameter("password");
                    if (newPassword != null && newPassword.length() >= 8) {
                        String hashedPassword = PasswordUtils.hashPassword(newPassword);
                        userDao.updatePassword(userId, hashedPassword);
                        session.invalidate();
                        response.sendRedirect(request.getContextPath() + "/login?message=PasswordUpdated");
                        return;
                    } else {
                        request.setAttribute("error", "Password must be at least 8 characters long.");
                    }
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                    return;
            }
            response.sendRedirect("settings");
        } catch (DbException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServletException("Ошибка при обновлении настроек пользователя", e);
        }
    }
}

