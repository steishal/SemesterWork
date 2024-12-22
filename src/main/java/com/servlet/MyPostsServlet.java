package com.servlet;

import com.dao.CommentDao;
import com.dao.LikeDao;
import com.dao.PostDao;
import com.dao.UserDao;
import com.models.Comment;
import com.models.Like;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");
        CommentDao commentDao = (CommentDao) context.getAttribute("commentDao");
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        LikeDao likeDao = (LikeDao) context.getAttribute("likeDao");

        HttpSession session = req.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String username1 = (String) session.getAttribute("username");

        req.setAttribute("userId", userId);
        req.setAttribute("username1", username1);


        if (postDao == null) {
            throw new ServletException("PostDao is not initialized in the servlet context.");
        }
        if (commentDao == null || userDao == null || likeDao == null) {
            throw new ServletException("One or more DAOs are not initialized in the servlet context.");
        }

        try {
            List<Post> posts = postDao.getAllPosts();

            List<Post> userPosts = posts.stream()
                    .filter(post -> post.getUserId() == userId)
                    .collect(Collectors.toList());

            for (Post post : userPosts) {
                try {
                    List<String> images = postDao.getImagesForPost(post.getId());
                    post.setImages(images);
                } catch (DbException e) {
                    e.printStackTrace();
                    post.setImages(new ArrayList<>());
                }

                List<Like> likes = likeDao.getLikesByPostId(String.valueOf(post.getId()));
                List<Comment> comments = commentDao.getCommentsByPostId(String.valueOf(post.getId()));

                int likeCount = (likes != null) ? likes.size() : 0;
                int commentCount = (comments != null) ? comments.size() : 0;

                User author = userDao.getUserById(post.getUserId());
                String username = author.getUsername();

                String profileUrl = req.getContextPath() + "/profile?id=" + author.getId();

                req.setAttribute("likesCount" + post.getId(), likeCount);
                req.setAttribute("commentsCount" + post.getId(), commentCount);
                req.setAttribute("authorName" + post.getId(), username);
                req.setAttribute("authorProfileUrl" + post.getId(), profileUrl);
            }

            req.setAttribute("posts", userPosts);

            for (Post post : userPosts) {
                boolean userLiked = likeDao.isUserLiked(post.getId(), userId);
                req.setAttribute("userLiked" + post.getId(), userLiked);
            }

            req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);

        } catch (DbException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading posts");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        LikeDao likeDao = (LikeDao) context.getAttribute("likeDao");

        if (likeDao == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Internal server error\"}");
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        int postId = Integer.parseInt(req.getParameter("postId"));
        int userId = (Integer) session.getAttribute("userId");

        try {
            boolean liked = likeDao.toggleLike(postId, userId);
            resp.getWriter().write("{\"liked\":" + liked + "}");
        } catch (DbException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error processing the like action\"}");
        }
    }
}