package com.servlet;

import com.dao.CommentDao;
import com.dao.LikeDao;
import com.dao.PostDao;
import com.dao.UserDao;
import com.models.Comment;
import com.models.Post;
import com.models.User;
import com.utils.DbException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");
        CommentDao commentDao = (CommentDao) context.getAttribute("commentDao");
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        LikeDao likeDao = (LikeDao) context.getAttribute("likeDao");
        String postId = request.getParameter("postId");
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");
        String username1 = (String) session.getAttribute("username");
        Post post;
        try {
            post = postDao.getPostById(Integer.parseInt(postId));
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
        User postAuthor;
        try {
            postAuthor = userDao.getUserById(post.getUserId());
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("postId", postId);
        request.setAttribute("userId", userId);
        request.setAttribute("username1", username1);
        request.setAttribute("post", post);
        request.setAttribute("postAuthor", postAuthor);
        if (postId != null) {
            try {
                List<Comment> comments = commentDao.getCommentsByPostId(postId);
                request.setAttribute("comments", comments);
                request.getRequestDispatcher("/WEB-INF/comments.jsp").forward(request, response);
            } catch (DbException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load comments");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Post ID is required");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");
        CommentDao commentDao = (CommentDao) context.getAttribute("commentDao");
        UserDao userDao = (UserDao) context.getAttribute("userDao");
        LikeDao likeDao = (LikeDao) context.getAttribute("likeDao");
        String postId = request.getParameter("postId");
        String userId = request.getParameter("userId");
        String content = request.getParameter("content");
        if (postId != null && userId != null && content != null) {
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setAuthorId(userId);
            comment.setContent(content);
            try {
                commentDao.addComment(comment);
                response.sendRedirect(request.getContextPath() + "/comments?postId=" + postId);
            } catch (DbException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add comment");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
        }
    }
}
