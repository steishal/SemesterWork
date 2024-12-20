package com.servlet;

import com.dao.PostDao;
import com.models.Post;
import com.utils.DbException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;

@WebServlet("/editPost")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 50,
        maxRequestSize = 1024 * 1024 * 100
)
public class EditPostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");

        if (postDao == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PostDao не инициализирован.");
            return;
        }

        try {
            int postId = Integer.parseInt(req.getParameter("id"));
            Post post = postDao.getPostById(postId);

            if (post == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Пост не найден.");
                return;
            }

            req.setAttribute("post", post);
            System.out.println(post);
            req.getRequestDispatcher("/WEB-INF/edit_post.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный идентификатор поста.");
        } catch (DbException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при загрузке поста.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        PostDao postDao = (PostDao) context.getAttribute("postDao");

        if (postDao == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PostDao не инициализирован.");
            return;
        }
        System.out.println(req.getParameter("id"));
        HttpSession session = req.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Необходима авторизация.");
            return;
        }

        try {
            int postId = Integer.parseInt(req.getParameter("id"));
            String newContent = req.getParameter("content");
            System.out.println(newContent);
            Post post = postDao.getPostById(postId);
            System.out.println(post);

            if (post != null && post.getUserId().equals(userId)) {
                post.setContent(newContent);
                postDao.updatePost(post);
                resp.sendRedirect(req.getContextPath() + "/mypost");
            } else {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Вы не можете редактировать этот пост.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный идентификатор поста.");
        } catch (DbException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при обновлении поста.");
        }
    }
}

