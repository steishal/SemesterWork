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
        String userId = request.getParameter("id");
        if (userId == null || userId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
            return;
        }
        if (userDao == null) {
            throw new ServletException("UserDao is not initialized in the servlet context.");
        }
        HttpSession session = request.getSession(false);
        Integer requestedUserId = Integer.parseInt(userId);
        Integer myUserId = (Integer) session.getAttribute("userId");
        boolean isFollowing;
        try {
            isFollowing = userDao.isFollowing(myUserId, requestedUserId);
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("isFollowing", isFollowing);
        try {
            User user = userDao.getUserById(requestedUserId);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
                return;
            }
            int followerCount = userDao.countFollowers(requestedUserId);
            int subscriptionCount = userDao.countSubscriptions(requestedUserId);
            request.setAttribute("followerCount", followerCount);
            request.setAttribute("subscriptionCount", subscriptionCount);
            request.setAttribute("user", user);
            if (requestedUserId.equals(myUserId)) {
                request.getRequestDispatcher("/WEB-INF/myProfile.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
            }
        } catch (DbException e) {
            logger.log(Level.SEVERE, "Error retrieving user with ID: " + userId, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load profile.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please log in to continue.");
            return;
        }

        Integer myUserId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        String targetUserIdStr = request.getParameter("targetUserId");

        if (myUserId == null || targetUserIdStr == null || action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
            return;
        }

        int targetUserId = Integer.parseInt(targetUserIdStr);

        try {
            if ("follow".equals(action)) {
                userDao.followUser(myUserId, targetUserId);
            } else if ("unfollow".equals(action)) {
                userDao.unfollowUser(myUserId, targetUserId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                return;
            }
            response.sendRedirect("profile?id=" + targetUserId);
        } catch (DbException e) {
            logger.log(Level.SEVERE, "Error processing follow/unfollow action", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to process request.");
        }
    }
}

