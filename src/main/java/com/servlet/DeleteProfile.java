package com.servlet;

import com.dao.UserDao;
import com.models.User;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/deleteProfile")
public class DeleteProfile extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        User currentUser = null;
        try {
            currentUser = userDao.getUserById(userId);
        } catch (DbException e) {
            throw new RuntimeException(e);
        }

        if (currentUser != null && Boolean.TRUE.equals(session.getAttribute("passwordVerified"))) {
            try {
                userDao.deleteUser(currentUser.getId());
            } catch (DbException e) {
                throw new RuntimeException(e);
            }
            request.getSession().invalidate(); // Завершаем сессию пользователя
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            response.sendRedirect("/settings?error=incorrectPassword");
        }
    }
}

