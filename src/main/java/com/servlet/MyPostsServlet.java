package com.servlet;
import com.dao.UserDao;
import com.dao.PostDao;
import com.models.Post;
import com.models.User;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/mypost")
public class MyPostsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");

        if (postDao == null) {
            throw new ServletException("PostDao is not initialized in the servlet context.");
        }

        UserDao userDao = (UserDao) context.getAttribute("userDao");

        if (userDao == null) {
            throw new ServletException("UserDao is not initialized in the servlet context.");
        }

        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        User user = null;
        try {
            user = userDao.getUserById(userId);
        } catch (DbException e) {
            throw new ServletException("Error retrieving user.", e);
        }

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
            return;
        }

        List<Post> posts = null;
        try {
            posts = postDao.getAllPosts();
        } catch (DbException e) {
            throw new ServletException("Error loading posts.", e);
        }

        List<Post> userPosts = posts.stream()
                .filter(post -> post.getUserId() == userId)
                .collect(Collectors.toList());

        for (Post post : userPosts) {
            try {
                List<String> images = postDao.getImagesForPost(post.getId());
                post.setImages(images);
            } catch (DbException e) {
                getServletContext().log("Error retrieving images for post: " + post.getId(), e);
                post.setImages(new ArrayList<>());
            }
        }

        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/WEB-INF/main.jsp").forward(request, response);
    }
}

