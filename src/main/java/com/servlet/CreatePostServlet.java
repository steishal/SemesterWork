package com.servlet;

import com.dao.PostDao;
import com.dao.UserDao;
import com.models.Post;
import com.models.User;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/createPost")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class CreatePostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/createPost.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");
        UserDao userDao = (UserDao) context.getAttribute("userDao");

        if (postDao == null) {
            throw new ServletException("PostDao is not initialized in the servlet context.");
        }

        try {
            String content = request.getParameter("content");
            System.out.println("Content: " + request.getParameter("content"));
            String categoryIdStr = request.getParameter("categoryId");

            if (content == null || content.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Content cannot be empty.");
                return;
            }

            if (categoryIdStr == null || categoryIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category ID cannot be empty.");
                return;
            }

            int categoryId = Integer.parseInt(categoryIdStr);

            List<InputStream> imageStreams = new ArrayList<>();
            List<String> imageNames = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                Part part = request.getPart("image" + i);
                System.out.println("Checking part with name: image" + i);
                if (part != null) {
                    System.out.println("Found part: " + part.getSubmittedFileName());
                    if (part.getSize() > 0) {
                        imageStreams.add(part.getInputStream());
                        imageNames.add(part.getSubmittedFileName());
                    } else {
                        System.out.println("No content in this file.");
                    }
                } else {
                    System.out.println("Part with name image" + i + " is null.");
                }
            }


            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User session not valid.");
                return;
            }

            Integer userId = (Integer) session.getAttribute("userId");
            User user = userDao.getUserById(userId);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
                return;
            }
            String uploadDirPath = request.getServletContext().getRealPath("/uploads");

            Post post = new Post();
            post.setUserId(userId);
            post.setCategoryId(categoryId);
            post.setContent(content);

            postDao.savePost(post, imageStreams, imageNames, uploadDirPath);

            response.sendRedirect(request.getContextPath() + "/main");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid categoryId.");
        } catch (DbException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error.");
        }
    }
}


