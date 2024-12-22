package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/editProfile")
public class EditProfile extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer myUserId = (Integer) session.getAttribute("userId");
        try {
            User user = userDao.getUserById(myUserId);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/edit_profile.jsp").forward(request, response);
        } catch (DbException e) {
            throw new ServletException("Ошибка получения данных пользователя", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String phoneNumber = request.getParameter("phoneNumber");
        String vkLink = request.getParameter("vkLink");
        String tgLink = request.getParameter("tgLink");

        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        HttpSession session = request.getSession(false);

        Integer myUserId = (Integer) session.getAttribute("userId");
        User user;
        try {
            user = userDao.getUserById(myUserId);
        } catch (DbException e) {
            throw new ServletException("Ошибка получения данных пользователя", e);
        }

        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);
        user.setVkLink(vkLink);
        user.setTgLink(tgLink);

        try {
            userDao.updateUser(user);
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/profile?id=" + user.getId());
        } catch (DbException e) {
            throw new ServletException("Ошибка при обновлении профиля", e);
        }
    }
}


