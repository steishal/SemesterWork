package com.servlet;

import com.dao.PostDao;
import com.models.Post;
import com.utils.DbException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/main")
public class MainPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");

        if (postDao == null) {
            throw new ServletException("PostDao is not initialized in the servlet context.");
        }

        try {

            List<Post> posts = postDao.getAllPosts();

            for (Post post : posts) {
                try {
                    List<String> images = postDao.getImagesForPost(post.getId());
                    post.setImages(images);
                } catch (DbException e) {
                    e.printStackTrace();
                    post.setImages(new ArrayList<>());
                }
            }

            req.setAttribute("posts", posts);

            req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
        } catch (DbException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading posts");
        }
    }
}



