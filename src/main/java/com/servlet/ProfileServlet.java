package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");

        if (userDao == null) {
            throw new ServletException("UserDao is not initialized in the servlet context.");
        }

        HttpSession session = request.getSession(false);

        Integer userId = (Integer) session.getAttribute("userId");
        try {
            User user = userDao.getUserById(userId);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
                return;
            }

            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
        } catch (DbException e) {
            logger.log(Level.SEVERE, "Error retrieving user with ID: " + userId, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load profile.");
        }
    }
}

