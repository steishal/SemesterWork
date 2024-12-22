package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.service.UserService;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/confirmPassword")
public class ConfirmPasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/passwordConfirmation.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        UserService userService = (UserService) context.getAttribute("userService");

        if (userDao == null || userService == null) {
            throw new ServletException("userDao или userService отсутствуют в контексте.");
        }

        String enteredPassword = request.getParameter("password");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        if (enteredPassword == null || enteredPassword.isEmpty()) {
            request.setAttribute("message", "Пароль не может быть пустым.");
            request.getRequestDispatcher("/WEB-INF/passwordConfirmation.jsp").forward(request, response);
            return;
        }

        try {
            User currentUser = userDao.getUserById(userId);

            if (currentUser == null || !userService.verifyPassword(enteredPassword, currentUser.getPassword())) {
                request.setAttribute("message", "Неверный пароль.");
                request.getRequestDispatcher("/WEB-INF/passwordConfirmation.jsp").forward(request, response);
                return;
            }
            session.setAttribute("passwordVerified", true);
            response.sendRedirect(request.getContextPath() + "/settings");
        } catch (DbException e) {
            throw new ServletException("Ошибка при подтверждении пароля", e);
        }
    }
}


